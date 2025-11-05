package com.A105.prham.webhook.service;

// ✨ 필요한 임포트 추가
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
	private final DeadlineParserService dateParserService; // (이름을 변경하셨다면 수정 필요)

	private final MattermostFileService fileService;
	// private final LLMClassificationService llmService;

	@Async
	@TransactionalEventListener
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleMessageReceived(PostReceivedEvent event) {
		Long postId = event.getMessageId();
		log.info("[Async] Processing post ID: {}", postId);

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new RuntimeException("Post not found: " + postId));

		try {
			// 1. 상태 변경: PROCESSING
			post.setStatus(PostStatus.PROCESSING);
			// ✨ 이 시점의 save는 PROCESSING 상태만 업데이트 (선택 사항)
			// postRepository.save(post);

			// ✨ 2. 파일 처리 (실제 로직으로 대체)
			String extractedFileText = ""; // (Tika 등으로 텍스트 추출 시 여기에 저장)

			String fileIdsString = post.getFileIds(); // "id1,id2,id3"

			if (StringUtils.hasText(fileIdsString)) {
				log.info("[Async] Found files to process: {}", fileIdsString);
				List<String> fileIdList = Arrays.asList(fileIdsString.split(","));

				for (String fileId : fileIdList) {
					if (StringUtils.hasText(fileId)) {
						// MattermostFileService를 호출하여 파일 다운로드 및 File 엔티티 생성
						File fileEntity = fileService.downloadAndSaveFile(fileId.trim()); // 공백 제거

						if (fileEntity != null) {
							// Post.java의 addFile 메서드를 사용하여 연관관계 설정
							post.addFile(fileEntity);
							log.info("File entity created for mmFileId: {}", fileId);
						}
					}
				}
				log.info("[Async] Finished processing {} files.", fileIdList.size());
			}

			// 3. 텍스트 전처리
			String cleanedText = emojiRemovalService.removeEmojis(post.getOriginalText());
			LocalDateTime deadlineDt = dateParserService.parseDeadline(cleanedText);
			String deadline = (deadlineDt != null) ? deadlineDt.toString() : null;

			// 4. LLM 분류 (STUB)
			String combinedText = cleanedText + "\n" + extractedFileText;
			String category = "학사-정보"; // 임시

			// 5. DB에 최종 결과 업데이트
			post.setCleanedText(cleanedText);
			post.setDeadline(deadline);
			post.setCategory(category);
			// post.setStorageFileUrls(...); // ✨ 이 줄은 File 엔티티를 사용하므로 불필요
			post.setStatus(PostStatus.PROCESSED);
			post.setProcessedAt(LocalDateTime.now().toString());

			// ✨ 마지막 save 한 번으로 Post 업데이트와 File 신규 저장이 동시에 일어남 (Cascade)
			postRepository.save(post);
			log.info("[Async] Successfully processed post ID: {}", postId);

		} catch (Exception e) {
			log.error("[Async] Failed to process post ID: {}", postId, e);
			post.setStatus(PostStatus.FAILED);
			postRepository.save(post);
		}
	}
}