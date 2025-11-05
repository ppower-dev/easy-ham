package com.A105.prham.search.service;

import com.A105.prham.search.dto.request.PostSearchRequest;
import com.A105.prham.webhook.entity.Post;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.SearchResult;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void indexPost(Post post) {
        try {
            Index index = meilisearchClient.index(INDEX_NAME);
            Map<String, Object> document = postToDocument(post);

            String json = objectMapper.writeValueAsString(List.of(document));
            index.addDocuments(json);
            log.info("📝 JSON to send: {}", json);
            log.info("✅ Indexed post: {}", post.getId());
        } catch (Exception e) {
            log.error("❌ Failed to index post: {}", post.getId(), e);
            throw new RuntimeException("Failed to index post", e);
        }
    }

    public void indexPosts(List<Post> posts) {
        try {
            Index index = meilisearchClient.index(INDEX_NAME);

            List<Map<String, Object>> documents = posts.stream()
                    .map(this::postToDocument)
                    .collect(Collectors.toList());

            String json = objectMapper.writeValueAsString(documents);
            index.addDocuments(json);

            log.info("✅ Indexed {} posts", posts.size());
        } catch (Exception e) {
            log.error("❌ Failed to index posts", e);
            throw new RuntimeException("Failed to index posts", e);
        }
    }

    public SearchResult searchPosts(PostSearchRequest request) {
        try {
            Index index = meilisearchClient.index(INDEX_NAME);

            // 검색어가 비어있으면 빈 결과 반환 (선택적)
            if (request.getKeyword() == null || request.getKeyword().trim().isEmpty()) {
                log.warn("⚠️ Empty keyword - returning filtered results only");
                // 키워드 없이는 필터만으로 검색하거나, 빈 결과 반환
            }

            String filter = buildFilter(request);
            String[] sort = new String[]{request.getSort()};

            SearchRequest.SearchRequestBuilder builder = SearchRequest.builder()
                    .q(request.getKeyword() != null ? request.getKeyword().trim() : "")
                    .limit(request.getSize())
                    .offset(request.getOffset())
                    .sort(sort)
                    .attributesToHighlight(new String[]{"content", "userName"})
                    .showMatchesPosition(true);  // 매칭 위치 표시 (디버깅용)

            if (!filter.isEmpty()) {
                builder.filter(new String[]{filter});
            }

            SearchRequest searchRequest = builder.build();

            // 디버깅 로그
            log.info("   - Keyword: '{}'", request.getKeyword());
            log.info("   - Filter: {}", filter.isEmpty() ? "(none)" : filter);
            log.info("   - Limit: {}, Offset: {}", request.getSize(), request.getOffset());

            SearchResult result = (SearchResult) index.search(searchRequest);

            log.info("✅ Search completed: {} results found", result.getHits().size());

            return result;

        } catch (Exception e) {
            log.error("❌ Failed to search posts with keyword: '{}'", request.getKeyword(), e);
            throw new RuntimeException("Failed to search posts", e);
        }
    }

    public void deletePost(Long postId) {
        try {
            Index index = meilisearchClient.index(INDEX_NAME);
            index.deleteDocument(String.valueOf(postId));
            log.info("✅ Deleted post: {}", postId);
        } catch (Exception e) {
            log.error("❌ Failed to delete post: {}", postId, e);
            throw new RuntimeException("Failed to delete post", e);
        }
    }

    public Object getIndexStats() {
        try {
            Index index = meilisearchClient.index(INDEX_NAME);
            return index.getStats();
        } catch (Exception e) {
            log.error("Failed to get index stats", e);
            throw new RuntimeException("Failed to get index stats", e);
        }
    }

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

    private Map<String, Object> postToDocument(Post post) {
        Map<String, Object> document = new HashMap<>();
        document.put("id", post.getId());
        document.put("mmMessageId", post.getMmMessageId());
        document.put("mmChannelId", post.getMmChannelId());
        document.put("userName", post.getUserName());
        document.put("content", post.getContent());
        document.put("mmCreatedAt", post.getMmCreatedAt());

        if (post.getNotice() != null) {
            document.put("mainCategory", post.getNotice().getMaincode());
            document.put("subCategory", post.getNotice().getSubcode());
        } else {
            document.put("mainCategory", null);
            document.put("subCategory", null);
        }

        return document;
    }

    private String escapeFilterValue(String str) {
        return str.replace("'", "\\'");
    }
}