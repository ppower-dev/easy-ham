package com.A105.prham.messages.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Slf4j
public class EmojiRemovalService {

    // 이모지 패턴들
    private static final Pattern EMOJI_PATTERN = Pattern.compile(":[a-zA-Z0-9_+-]+:");
    private static final Pattern UNICODE_EMOJI_PATTERN = Pattern.compile(
            "[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+|[\\u2600-\\u26FF]|[\\u2700-\\u27BF]"
    );

    public String removeEmojis(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        // :emoji_name: 형태 제거
        String cleaned = EMOJI_PATTERN.matcher(text).replaceAll("");

        // 유니코드 이모지 제거
        cleaned = UNICODE_EMOJI_PATTERN.matcher(cleaned).replaceAll("");

        // 여러 공백을 하나로 정리
        cleaned = cleaned.replaceAll("\\s+", " ").trim();

        log.debug("Emoji removal: '{}' -> '{}'", text, cleaned);

        return cleaned;
    }
}