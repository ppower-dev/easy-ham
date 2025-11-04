package com.A105.prham.search.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSearchRequest {
    private String keyword;
    private String channelId;
    private String mainCategory;
    private String subCategory;
    private Long startDate;
    private Long endDate;
    private String sort = "mmCreatedAt:desc"; // 기본값
    private int page = 0;
    private int size = 20;

    public int getOffset() {
        return page * size;
    }
}