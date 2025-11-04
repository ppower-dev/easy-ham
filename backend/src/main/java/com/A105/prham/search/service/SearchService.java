package com.A105.prham.search.service;


import com.A105.prham.search.dto.request.PostSearchRequest;
import com.A105.prham.webhook.entity.Post;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.SearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final Client meilisearchClient;
    private static final String INDEX_NAME = "posts";

    // 단일 Post 색인
    public void indexPost(Post post) {
        try {
            Index index = meilisearchClient.index(INDEX_NAME);
            Map<String, Object> document = postToDocument(post);
            index.addDocuments("[" + toJson(document) + "]");
            log.debug("Indexed post: {}", post.getId());
        } catch (Exception e) {
            log.error("Failed to index post: {}", post.getId(), e);
            throw new RuntimeException("Failed to index post", e);
        }
    }

    // 여러 Post 일괄 색인
    public void indexPosts(List<Post> posts) {
        try {
            Index index = meilisearchClient.index(INDEX_NAME);
            List<Map<String, Object>> documents = posts.stream()
                    .map(this::postToDocument)
                    .collect(Collectors.toList());

            String json = documents.stream()
                    .map(this::toJson)
                    .collect(Collectors.joining(",", "[", "]"));

            index.addDocuments(json);
            log.info("Indexed {} posts", posts.size());
        } catch (Exception e) {
            log.error("Failed to index posts", e);
            throw new RuntimeException("Failed to index posts", e);
        }
    }

    // 고급 검색
    public SearchResult searchPosts(PostSearchRequest request) {
        try {
            Index index = meilisearchClient.index(INDEX_NAME);

            // 필터 조건 생성
            String filter = buildFilter(request);

            // 정렬 조건
            String[] sort = new String[]{request.getSort()};

            SearchRequest.SearchRequestBuilder builder = SearchRequest.builder()
                    .q(request.getKeyword() != null ? request.getKeyword() : "")
                    .limit(request.getSize())
                    .offset(request.getOffset())
                    .sort(sort)
                    .attributesToHighlight(new String[]{"content", "userName"});

            // 필터가 있을 때만 추가
            if (!filter.isEmpty()) {
                builder.filter(new String[]{filter});
            }

            return (SearchResult) index.search(builder.build());
        } catch (Exception e) {
            log.error("Failed to search posts", e);
            throw new RuntimeException("Failed to search posts", e);
        }
    }

    // Post 삭제
    public void deletePost(Long postId) {
        try {
            Index index = meilisearchClient.index(INDEX_NAME);
            index.deleteDocument(String.valueOf(postId));
            log.debug("Deleted post from index: {}", postId);
        } catch (Exception e) {
            log.error("Failed to delete post from index: {}", postId, e);
            throw new RuntimeException("Failed to delete post", e);
        }
    }

    // 인덱스 통계 조회
    public Object getIndexStats() {
        try {
            Index index = meilisearchClient.index(INDEX_NAME);
            return index.getStats();
        } catch (Exception e) {
            log.error("Failed to get index stats", e);
            throw new RuntimeException("Failed to get index stats", e);
        }
    }

    // 필터 조건 생성
    private String buildFilter(PostSearchRequest request) {
        List<String> filters = new ArrayList<>();

        if (request.getChannelId() != null && !request.getChannelId().isBlank()) {
            filters.add("mmChannelId = '" + escapeFilterValue(request.getChannelId()) + "'");
        }

        if (request.getMainCategory() != null && !request.getMainCategory().isBlank()) {
            filters.add("mainCategory = '" + escapeFilterValue(request.getMainCategory()) + "'");
        }

        if (request.getSubCategory() != null && !request.getSubCategory().isBlank()) {
            filters.add("subCategory = '" + escapeFilterValue(request.getSubCategory()) + "'");
        }

        // 날짜 범위 필터
        if (request.getStartDate() != null && request.getEndDate() != null) {
            filters.add("mmCreatedAt >= " + request.getStartDate() +
                    " AND mmCreatedAt <= " + request.getEndDate());
        } else if (request.getStartDate() != null) {
            filters.add("mmCreatedAt >= " + request.getStartDate());
        } else if (request.getEndDate() != null) {
            filters.add("mmCreatedAt <= " + request.getEndDate());
        }

        return String.join(" AND ", filters);
    }

    // Post를 Document로 변환
    private Map<String, Object> postToDocument(Post post) {
        Map<String, Object> document = new HashMap<>();
        document.put("id", post.getId());
        document.put("mmMessageId", post.getMmMessageId());
        document.put("mmChannelId", post.getMmChannelId());
        document.put("userName", post.getUserName());
        document.put("content", post.getContent());
        document.put("mmCreatedAt", post.getMmCreatedAt());

        // 카테고리 필드 추가 (Notice가 있을 경우)
        if (post.getNotice() != null) {
            document.put("mainCategory", post.getNotice().getMaincode());
            document.put("subCategory", post.getNotice().getSubcode());
        } else {
            document.put("mainCategory", null);
            document.put("subCategory", null);
        }

        return document;
    }

    // Map을 JSON 문자열로 변환
    private String toJson(Map<String, Object> map) {
        return "{" + map.entrySet().stream()
                .map(e -> "\"" + e.getKey() + "\":" + formatValue(e.getValue()))
                .collect(Collectors.joining(",")) + "}";
    }

    // 값 포맷팅
    private String formatValue(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String) {
            return "\"" + escapeJson(value.toString()) + "\"";
        } else {
            return value.toString();
        }
    }

    // JSON 이스케이프
    private String escapeJson(String str) {
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // 필터 값 이스케이프
    private String escapeFilterValue(String str) {
        return str.replace("'", "\\'");
    }
}