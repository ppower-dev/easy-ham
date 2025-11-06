package com.A105.prham.webhook.service;

// ✨ 필요한 임포트 추가
import com.A105.prham.classification.dto.LlmClassificationResult;
import com.A105.prham.classification.service.LlmClassificationService;
import com.A105.prham.webhook.entity.File;
import com.A105.prham.webhook.entity.Post;
import com.A105.prham.webhook.entity.PostStatus;
import com.A105.prham.webhook.event.PostReceivedEvent;
import com.A105.prham.webhook.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.StringUtils; // ✨ StringUtils 임포트

import java.time.LocalDateTime;
import java.util.Arrays; // ✨ Arrays 임포트
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncPostProcessor {

	private final PostRepository postRepository;
	private final EmojiRemoveService emojiRemovalService;
	private final MattermostFileService fileService;
	private final LlmClassificationService llmService;

	@Async
	@TransactionalEventListener
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleMessageReceived(PostReceivedEvent event) {
		Long postId = event.getMessageId();
		log.info("[Async] Processing post ID: {}", postId);

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new RuntimeException("Post 없음: " + postId));

		//파일 처리 실패 추적 플러그
		boolean fileProcessingFailed = false;

		try {
			// 1. 상태 변경: PROCESSING
			post.setStatus(PostStatus.PROCESSING);

			String fileIdsString = post.getFileIds();

			if (StringUtils.hasText(fileIdsString)) {
				log.info("[비동기] 처리할 파일 존재: {}", fileIdsString);
				List<String> fileIdList = Arrays.asList(fileIdsString.split(","));

				for(String fileId : fileIdList) {
					if (StringUtils.hasText(fileId)) {
						File fileEntity = fileService.getFileInfoAndCreateEntity(fileId.trim());

						if(fileEntity != null) {
							post.addFile(fileEntity);
							log.info("파일 엔티티 만들어졌음: {}", fileId);
						} else {
							log.warn("[비동기] 파일 처리 실패, 파일 아이디: {}", fileId);
							fileProcessingFailed = true;
						}
					}
				}
			}

			// 3. 텍스트 전처리
			String cleanedText = emojiRemovalService.removeEmojis(post.getOriginalText());
			post.setCleanedText(cleanedText); //llm 전 원본 메시지 저장

			LlmClassificationResult result = llmService.classify(post);

			if(result == null) {
				throw new RuntimeException("llm 분류 실패");
			}

			// 5. DB에 최종 결과 업데이트
			post.setTitle(result.getTitle());
			post.setMainCategory(result.getMainCategory());
			post.setSubCategory(result.getSubCategory());
			post.setDeadline(result.getDeadline());

			if (result.getCampusList() != null && !result.getCampusList().isEmpty()) {
				post.setCampusList(String.join(",", result.getCampusList()));
			}

			if(fileProcessingFailed) {
				post.setStatus(PostStatus.FAILED);
				log.warn("[비동기] 파일 에러로 post 처리 실패, post 아이디: {}", postId);
			}else {
				post.setStatus(PostStatus.PROCESSED);
				log.info("[비동기] post 처리 성공, post 아이디: {}", postId );
			}

			post.setProcessedAt(LocalDateTime.now().toString());
			postRepository.save(post);
		} catch (Exception e) {
			log.error("[비동기] post 처리 실패: {}", postId, e);
			if (post.getStatus() != PostStatus.FAILED) {
				post.setStatus(PostStatus.FAILED);
				postRepository.save(post);
			}
		}
	}
}