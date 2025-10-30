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

        // 3. ProcessedMessage 생성
        ProcessedMessage processed = new ProcessedMessage();
        processed.setPostId(payload.getPostId());
        processed.setChannelId(payload.getChannelId());
        processed.setUserId(payload.getUserId());
        processed.setTimestamp(payload.getTimestamp());
        processed.setOriginalText(payload.getText());
        processed.setCleanedText(cleanedText);
        processed.setDeadline(deadline);
        processed.setProcessedAt(LocalDateTime.now().toString());

        log.info("Preprocessing completed - Deadline: {}", deadline);

        return processed;
    }
}