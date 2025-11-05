package com.A105.prham.messages.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProcessedMessage {

    private String postId;
    private String channelId;
    private String userId;
    private Long timestamp;

    // 메시지 내용
    private String originalText;    // 원본 텍스트
    private String cleanedText;     // 이모지 제거된 텍스트

    // 파싱된 정보
    private String deadline;  // 파싱된 마감일

    // 카테고리 정보 (새로 추가)
    private Long mainCategory;      // 메인 카테고리
    private Long subCategory;       // 서브 카테고리

    // 메타데이터
    private String processedAt;
}