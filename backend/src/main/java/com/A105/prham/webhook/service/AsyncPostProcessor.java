package com.A105.prham.webhook.service;

// ✨ 임포트가 대대적으로 수정되어야 합니다.
import com.A105.prham.webhook.entity.Post; // Message -> Post
import com.A105.prham.webhook.entity.PostStatus; // MessageStatus -> PostStatus
import com.A105.prham.webhook.event.PostReceivedEvent; // MessageReceivedEvent -> PostReceivedEvent
import com.A105.prham.webhook.repository.PostRepository; // MessageRepository -> PostRepository


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncPostProcessor { // ✨ 클래스명 오타 수정 (Proceesor -> Processor)

	private final PostRepository postRepository; // ✨ 수정
	private final EmojiRemoveService emojiRemoveService;
	private final DeadlineParserService dateParserService;

	// --- 주입 ---
	// private final MattermostFileService fileService;
	// private final LLMClassificationService llmService;

	@Async
	@TransactionalEventListener
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleMessageReceived(PostReceivedEvent event) { // ✨ 수정
		Long postId = event.getMessageId(); // ✨ 변수명 (messageId -> postId)
		log.info("[Async] Processing post ID: {}", postId);

		// ✨ Post 엔티티로 조회 (ID로 조회 시에는 files가 LAZY 로딩됨)
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new RuntimeException("Post not found: " + postId));

		try {
			// 1. 상태 변경: PROCESSING
			post.setStatus(PostStatus.PROCESSING); // ✨ 수정
			postRepository.save(post);

			// 2. 파일 처리 (STUB)
			// (참고: Post.java 수정으로 fileService 로직이 변경되어야 함)
			// String fileIdsString = post.getFiles().stream().map(File::getMmFileId).collect(Collectors.joining(","));
			// List<String> s3Urls = fileService.downloadAndStoreFiles(fileIdsString);
			// String extractedFileText = fileService.extractTextFromFiles(s3Urls);
			List<String> s3Urls = Collections.emptyList();
			String extractedFileText = "";

			// 3. 텍스트 전처리
			String cleanedText = emojiRemoveService.removeEmojis(post.getOriginalText());
			LocalDateTime deadlineDt = dateParserService.parseDeadline(cleanedText);
			String deadline = (deadlineDt != null) ? deadlineDt.toString() : null;

			// 4. LLM 분류 (STUB)
			String combinedText = cleanedText + "\n" + extractedFileText;
			String category = "학사-정보"; // 임시

			// 5. DB에 최종 결과 업데이트
			post.setCleanedText(cleanedText);
			post.setDeadline(deadline);
			post.setCategory(category);
			post.setStorageFileUrls(String.join(",", s3Urls));
			post.setStatus(PostStatus.PROCESSED); // ✨ 수정
			post.setProcessedAt(LocalDateTime.now().toString());

			postRepository.save(post);
			log.info("[Async] Successfully processed post ID: {}", postId);

			// 6. (Optional) Elasticsearch 저장

		} catch (Exception e) {
			log.error("[Async] Failed to process post ID: {}", postId, e);
			post.setStatus(PostStatus.FAILED); // ✨ 수정
			postRepository.save(post);
		}
	}
}