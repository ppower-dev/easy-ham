package com.A105.prham.webhook.service;

// ✨ 모두 webhook 패키지의 클래스를 임포트하도록 수정
import com.A105.prham.webhook.dto.MattermostWebhookDto;
import com.A105.prham.webhook.dto.ProcessedWebhook;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList; // ✨ 추가
import java.util.Arrays; // ✨ 추가
import java.util.List; // ✨ 추가

@Service
@Slf4j
@RequiredArgsConstructor
public class PostProcessorService {

	private final EmojiRemoveService emojiRemoveService; // ✨ (파일명과 일치시킴)
	private final DeadlineParserService dateParserService;

	public ProcessedWebhook preprocess(MattermostWebhookDto payload) { // ✨ DTO 수정
		log.info("Preprocessing message: {}", payload.getPostId());

		// 1. 이모지 제거
		String cleanedText = emojiRemoveService.removeEmojis(payload.getText());

		// 2. 날짜 파싱
		LocalDateTime deadlineDateTime = dateParserService.parseDeadline(cleanedText);
		String deadline = deadlineDateTime != null ? deadlineDateTime.toString() : null;

		// 3. ProcessedWebhook 생성
		ProcessedWebhook processed = new ProcessedWebhook(); // ✨ DTO 수정
		processed.setPostId(payload.getPostId());
		processed.setChannelId(payload.getChannelId());
		processed.setTeamId(payload.getTeamId()); // ✨ (필요시)
		processed.setUserId(payload.getUserId());
		processed.setUserName(payload.getUserName()); // ✨ (필요시)
		processed.setTimestamp(payload.getTimestamp());
		processed.setOriginalText(payload.getText());
		processed.setCleanedText(cleanedText);
		processed.setDeadline(deadline);

		// ✨ 4. File ID 문자열을 List로 변환
		String fileIdsString = payload.getFileIds();
		List<String> fileIdList = new ArrayList<>();
		if (fileIdsString != null && !fileIdsString.isEmpty()) {
			fileIdList = Arrays.asList(fileIdsString.split(","));
		}
		processed.setFileIds(fileIdList); // ✨ List<String> 설정

		processed.setProcessedAt(LocalDateTime.now().toString());

		log.info("Preprocessing completed - Deadline: {}, FileIDs: {}", deadline, fileIdsString);

		return processed;
	}
}