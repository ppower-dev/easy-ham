package com.A105.prham.messages.service;

import com.A105.prham.messages.dto.MattermostWebhookDTO;
import com.A105.prham.messages.dto.ProcessedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageProcessorService {

    private final EmojiRemovalService emojiRemovalService;
    private final DateParserService dateParserService;

    public ProcessedMessage preprocess(MattermostWebhookDTO payload) {
        log.info("Preprocessing message: {}", payload.getPostId());

        // 1. 이모지 제거
        String cleanedText = emojiRemovalService.removeEmojis(payload.getText());

        // 2. 날짜 파싱 (LocalDateTime으로 파싱 후 String으로 변환)
        LocalDateTime deadlineDateTime = dateParserService.parseDeadline(cleanedText);
        String deadline = deadlineDateTime != null ? deadlineDateTime.toString() : null;

        // 3. timestamp를 Long으로 변환
        Long timestampLong = parseTimestamp(payload.getTimestamp());

        // 4. ProcessedMessage 생성
        ProcessedMessage processed = new ProcessedMessage();
        processed.setPostId(payload.getPostId());
        processed.setChannelId(payload.getChannelId());
        processed.setUserId(payload.getUserId());
        processed.setTimestamp(timestampLong);  // Long 타입으로 저장
        processed.setOriginalText(payload.getText());
        processed.setCleanedText(cleanedText);
        processed.setDeadline(deadline);
        processed.setProcessedAt(LocalDateTime.now().toString());

        // 5. TODO 카테고리 설정 (현재는 null, 추후 분류 로직 추가 가능)
        processed.setMainCategory(null);
        processed.setSubCategory(null);

        log.info("Preprocessing completed - Deadline: {}", deadline);

        return processed;
    }

    /**
     * Mattermost timestamp(String)를 Long(밀리초)으로 변환
     * Mattermost는 밀리초 단위의 timestamp를 String으로 보냄
     */
    private Long parseTimestamp(String timestamp) {
        if (timestamp == null || timestamp.isEmpty()) {
            return System.currentTimeMillis();
        }

        try {
            return Long.parseLong(timestamp);
        } catch (NumberFormatException e) {
            log.warn("Failed to parse timestamp: {}, using current time", timestamp);
            return System.currentTimeMillis();
        }
    }
}