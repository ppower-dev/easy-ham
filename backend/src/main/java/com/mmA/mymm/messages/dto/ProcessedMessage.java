package com.mmA.mymm.messages.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProcessedMessage {

    private String postId;
    private String channelId;
    private String userId;
    private String timestamp;

    // 메시지 내용
    private String originalText;    // 원본 텍스트
    private String cleanedText;     // 이모지 제거된 텍스트

    // 파싱된 정보
    private String deadline;  // 파싱된 마감일
    // 메타데이터
    private String processedAt;
}