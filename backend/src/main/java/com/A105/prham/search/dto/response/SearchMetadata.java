package com.A105.prham.search.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchMetadata {
    private String query;              // 검색어
    private int totalHits;             // 전체 결과 수
    private int page;                  // 현재 페이지 (0-based)
    private int size;                  // 페이지 크기
    private int totalPages;            // 전체 페이지 수
    private int processingTimeMs;      // 검색 처리 시간 (ms)
}