package com.A105.prham.search.controller;

import com.A105.prham.search.dto.request.PostSearchRequest;
import com.A105.prham.search.service.SearchService;
import com.meilisearch.sdk.model.SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/posts")
    public ResponseEntity<SearchResult> searchPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String channelId,
            @RequestParam(required = false) String mainCategory,
            @RequestParam(required = false) String subCategory,
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate,
            @RequestParam(defaultValue = "mmCreatedAt:desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PostSearchRequest request = new PostSearchRequest();
        request.setKeyword(keyword);
        request.setChannelId(channelId);
        request.setMainCategory(mainCategory);
        request.setSubCategory(subCategory);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setSort(sort);
        request.setPage(page);
        request.setSize(size);

        SearchResult result = searchService.searchPosts(request);
        return ResponseEntity.ok(result);
    }
}