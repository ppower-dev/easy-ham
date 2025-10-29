package com.A105.prham.webhook.service;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.A105.prham.common.config.MattermostConfig;
import com.A105.prham.webhook.dto.MattermostWebhookDto;
import com.A105.prham.webhook.entity.File;
import com.A105.prham.webhook.entity.Post;
import com.A105.prham.webhook.repository.FileRepository;
import com.A105.prham.webhook.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

	private final PostRepository postRepository;
	private final FileRepository fileRepository;
	private final MattermostApiService mattermostApiService;
	private final MattermostConfig mattermostConfig;

	// Webhook 메시지 처리
	// Post 저장
	@Async("webhookExecutor")
	@Transactional
	public void processWebhook(MattermostWebhookDto dto) {
		try{
			// null 검사와 유효성 검사
			validateWebhookPayload(dto);

			// 채널 필터링
			if (!mattermostConfig.isAllowedChannel(dto.channelId())) {
				log.info("🔍 Checking channel: {}, allowed channels: {}",
					dto.channelId(), mattermostConfig.getAllowedChannelList());				return;
			}

			// 중복 체크
			if(postRepository.existsByMmMessageId((dto.postId()))){
				log.debug("이미 있는 메시지인데~", dto.postId());
				return;
			}

			// 이모지 제거된 본문 추출
			String cleanedContent = dto.getCleanedText();

			// Post  저장
			Post post = Post.builder()
				.mmMessageId(dto.postId())
				.mmChannelId(dto.channelId())
				.mmUserId(dto.userId())
				.mmTeamId(dto.teamId())
				.userName(dto.userName())
				.mmCreatedAt(dto.timestamp())
				.content(cleanedContent)
				.build();

			Post savedPost = postRepository.save(post);

			// 파일 처리
			if(dto.fileIds() != null && !dto.fileIds().isEmpty()){
				processFiles(savedPost, dto.fileIds());
			}
		} catch (IllegalArgumentException e) {
			log.error("유효하지 않은 webhook", e.getMessage());
		}
		catch (Exception e) {
			// 비동기처리라 예외 안던짐
			log.error("에러 발생 비상!", dto != null ? dto.postId() : "null", e);
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
				log.error("파일 처리 실패", fileId, e);
			}
		}
	}

	// 모든 유효성 검사
	private void validateWebhookPayload(MattermostWebhookDto dto) {
		if (dto == null) {
			throw new IllegalArgumentException("Webhook 없는데용");
		}
		if (dto.postId() == null || dto.postId().isBlank()) {
			throw new IllegalArgumentException("postId 필요해용");
		}
		if (dto.channelId() == null || dto.channelId().isBlank()) {
			throw new IllegalArgumentException("channelId 필요해용");
		}
		if (dto.userId() == null || dto.userId().isBlank()) {
			throw new IllegalArgumentException("userId 필요해용");
		}
		if(dto.teamId() == null || dto.teamId().isBlank()){
			throw new IllegalArgumentException("teamId 필요해용");
		}
	}
}