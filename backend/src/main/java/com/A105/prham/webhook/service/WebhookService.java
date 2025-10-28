package com.A105.prham.webhook.service;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.A105.prham.webhook.dto.MattermostWebhookDto;
import com.A105.prham.webhook.entity.File;
import com.A105.prham.webhook.entity.Post;
import com.A105.prham.webhook.repository.FileRepository;
import com.A105.prham.webhook.repository.PostRepository;

import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WebhookService {

	private final PostRepository postRepository;
	private final FileRepository fileRepository;
	private final MattermostApiService mattermostApiService;

	// Webhook 메시지 처리
	// Post 저장
	@Async("webhookExecutor")
	@Transactional
	public void processWebhook(MattermostWebhookDto dto) {
		try{
			// 중복 체크
			if(postRepository.existsByMmMessageId((dto.postId()))){
				return;
			}

			// 이모지 제거된 본문 추출
			String cleanedContent = dto.getCleanedText();

			// Post  저장
			Post post = Post.builder()
				.mmMessageId(dto.postId())
				.mmChannelId(dto.channelId())
				.mmUserId(dto.userId())
				.content(cleanedContent)
				.build();

			Post savedPost = postRepository.save(post);

			// 파일 처리
			if(dto.fileIds() != null && !dto.fileIds().isEmpty()){
				processFiles(savedPost, dto.fileIds());
			}
		} catch (Exception e) {
			throw new RuntimeException("webhook 오류 발생! 비상!!", e);
		}
	}

	// 파일 정보 조회 및 저장
	private void processFiles(Post post, List<String> fileIds) {
		for(String fileId : fileIds){
			try {
				var fileInfo = mattermostApiService.getFileInfo(fileId);

				File file = File.builder()
					.mmFileId(fileId)
					.fileName(fileInfo.name())
					.fileUrl(fileInfo.downloadUrl())
					.mimeType(fileInfo.mimeType())
					.build();

				post.addFile(file);
				fileRepository.save(file);
			} catch (Exception e) {
				// 파일 실패는 전체 처리를 중단하지 않음
			}
		}
	}
}