package com.A105.prham.webhook.service;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.A105.prham.webhook.entity.File;
import com.A105.prham.webhook.entity.Post;
import com.A105.prham.webhook.repository.FileRepository;
import com.A105.prham.webhook.repository.PostRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WebhookService {

	private final PostRepository postRepository;
	private final FileRepository fileRepository;
	private final WebClient mattermostApiWebClient;
	// (2순위에서 구현할)
	// private final ClassificationService classificationService;

	@Async("webhookTaskExecutor")
	@Transactional
	public void processMessageAsync(MattermostPayloadDto payload) {
		try {
			if (postRepository.existsByMmMessageId(payload.postId())) {
				log.warn("이미 처리된 메시지(mmMessageId: {})입니다. 스킵합니다.", payload.postId());
				return;
			}

			// 1. (수정) 2개의 API를 병렬로 호출합니다.
			Mono<MattermostPostDto> postMono = fetchFullPost(payload.postId());
			Mono<String> authorNameMono = fetchAuthorName(payload.mmUserName())
				.onErrorReturn(payload.mmUserName()); // API 실패 시 fallback

			// 2. 2개 API의 응답이 모두 오면 통합합니다.
			Tuple2<MattermostPostDto, String> results = Mono.zip(postMono, authorNameMono).block();
			if (results == null) {
				log.error("MM API 호출에 실패했습니다 (postId: {})", payload.postId());
				return;
			}

			MattermostPostDto postObject = results.getT1();
			String authorName = results.getT2();

			// 3. (수정) 전처리 대상을 postObject.message()로 변경
			String preprocessedText = preprocessText(postObject.message());

			// 4. (1차 저장) 'Post' 엔티티 생성 및 저장
			Post post = Post.builder()
				.mmMessageId(postObject.id())
				.mmTeamId(payload.teamId()) // teamId는 웹훅 페이로드에만 있음
				.mmChannelId(postObject.channelId())
				.mmUserId(postObject.userId())
				.authorName(authorName) // 조회한 'nickname'
				.content(preprocessedText) // 전처리된 본문
				.mmCreatedAt(postObject.createAt()) // API로 조회한 생성 시간
				.build();

			Post savedPost = postRepository.save(post);
			log.info("새로운 Post 저장 완료: (ID: {}, Author Nickname: {})", savedPost.getId(), authorName);

			// 5. (수정) postObject.fileIds()를 사용해 파일 저장
			if (postObject.fileIds() != null && !postObject.fileIds().isEmpty()) {
				postObject.fileIds().forEach(mmFileId -> {
					File file = File.builder()
						.post(savedPost)
						.fileName(mmFileId) // (임시 - ERD 수정 권장)
						.fileUrl("pending_fetch")
						.build();
					fileRepository.save(file);
				});
			}

			// 6. (다음 단계) AI 분류 서비스 호출
			// classificationService.requestClassification(savedPost);

		} catch (Exception e) {
			log.error("Webhook 비동기 처리 중 심각한 오류 발생 (mmMessageId: {})", payload.postId(), e);
		}
	}

	/**
	 * (신규) 1. MM Post API (GET /posts/{id})를 호출하여 '전체 Post 객체'를 가져옵니다.
	 */
	private Mono<MattermostPostDto> fetchFullPost(String postId) {
		log.debug("MM Post API (GET) 호출 시작 (PostId: {})", postId);
		return mattermostApiWebClient.get()
			.uri("/api/v4/posts/{id}", postId)
			.retrieve()
			.bodyToMono(MattermostPostDto.class)
			.doOnSuccess(post -> log.debug("MM Post (GET) 조회 성공"));
	}

	/**
	 * (유지) 2. MM User API (POST /users/usernames)를 호출하여 'nickname'을 가져옵니다.
	 */
	private Mono<String> fetchAuthorName(String mmUserName) {
		log.debug("MM User API (POST) 호출 시작 (mmUserName: {})", mmUserName);
		List<String> requestBody = List.of(mmUserName);

		return mattermostApiWebClient.post()
			.uri("/api/v4/users/usernames")
			.bodyValue(requestBody)
			.retrieve()
			.bodyToMono(new ParameterizedTypeReference<List<MattermostUserDto>>() {})
			.map(userList -> (userList == null || userList.isEmpty()) ?
				mmUserName : userList.get(0).getNicknameToUse())
			.doOnSuccess(name -> log.debug("MM User nickname 조회 성공: {}", name))
			.doOnError(e -> log.error("MM User API (POST) 호출 실패 (mmUserName: {})", mmUserName, e));
	}

	/**
	 * (요구사항 반영) 텍스트 전처리
	 * - :emoji:
	 * - +:emoji: (Reaction)
	 */
	private String preprocessText(String text) {
		if (text == null || text.isBlank()) return "";

		// 1. 이모티콘 제거 (e.g., ":smile:", ":check:")
		String noEmojis = text.replaceAll(":[a-zA-Z0-9_\\-+]+:", "").trim();

		// 2. Reaction 제거 (e.g., " \n +:white_check_mark: 1")
		// (Reaction은 원본 message 필드에 포함되지 않고 metadata.reactions에만 있습니다.)
		// (다만, 기능 명세서에 있으니 방어 코드로 남겨둡니다.)
		String noReactions = noEmojis.replaceAll("(?m)^\\s*\\+:[a-zA-Z0-9_\\-+]+:.*$", "").trim();

		// 3. (추가) "----------" 같은 구분선 제거 (선택 사항)
		String cleanText = noReactions.replaceAll("----------", "").trim();

		return cleanText;
	}
}