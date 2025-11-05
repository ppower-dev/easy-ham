package com.A105.prham.search.controller;

import com.A105.prham.common.response.ApiResponseDto;
import com.A105.prham.common.response.ErrorCode;
import com.A105.prham.common.response.SuccessCode;
import com.A105.prham.search.dto.request.PostSearchRequest;
import com.A105.prham.search.dto.response.PostSearchResponse;
import com.A105.prham.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/posts")
    public ApiResponseDto<PostSearchResponse> searchPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String channelId,
            @RequestParam(required = false) Long subCategory,
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate,
            @RequestParam(defaultValue = "mmCreatedAt:desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try{
            PostSearchRequest request = new PostSearchRequest();
            request.setKeyword(keyword);
            request.setChannelId(channelId);
            request.setSubCategory(subCategory);
            request.setStartDate(startDate);
            request.setEndDate(endDate);
            request.setSort(sort);
            request.setPage(page);
            request.setSize(size);

            PostSearchResponse result = searchService.searchPosts(request);
            return ApiResponseDto.success(SuccessCode.SUCCESS, result);
        } catch (Exception e) {
            return ApiResponseDto.fail(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}