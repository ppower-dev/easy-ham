package com.A105.prham.search.service;

import com.A105.prham.search.dto.request.PostSearchRequest;
import com.A105.prham.search.dto.response.PostSearchItem;
import com.A105.prham.search.dto.response.PostSearchResponse;
import com.A105.prham.search.dto.response.SearchMetadata;
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
            log.info("📄 JSON to send: {}", json);
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

    public PostSearchResponse searchPosts(PostSearchRequest request) {
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
                    .showMatchesPosition(false);  // 매칭 위치는 프론트에서 불필요

            if (!filter.isEmpty()) {
                builder.filter(new String[]{filter});
            }

            SearchRequest searchRequest = builder.build();

            // 디버깅 로그
            log.info("🔍 Searching posts:");
            log.info("   - Keyword: '{}'", request.getKeyword());
            log.info("   - Filter: {}", filter.isEmpty() ? "(none)" : filter);
            log.info("   - Limit: {}, Offset: {}", request.getSize(), request.getOffset());

            SearchResult meilisearchResult = (SearchResult) index.search(searchRequest);

            log.info("✅ Search completed: {} results found in {}ms",
                    meilisearchResult.getHits().size(),
                    meilisearchResult.getProcessingTimeMs());

            // Meilisearch 결과를 커스텀 DTO로 변환
            return convertToResponse(meilisearchResult, request);

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

    public void deleteAllDocuments() {
        try {
            Index index = meilisearchClient.index(INDEX_NAME);
            index.deleteAllDocuments();
            log.info("✅ Deleted all documents from Meilisearch");
        } catch (Exception e) {
            log.error("❌ Failed to delete all documents", e);
            throw new RuntimeException("Failed to delete all documents", e);
        }
    }

    /**
     * Meilisearch SearchResult를 커스텀 DTO로 변환
     */
    private PostSearchResponse convertToResponse(SearchResult meilisearchResult, PostSearchRequest request) {
        // 검색 결과 아이템 변환
        List<PostSearchItem> items = meilisearchResult.getHits().stream()
                .map(this::convertToSearchItem)
                .collect(Collectors.toList());

        // 메타데이터 생성
        int totalHits = meilisearchResult.getEstimatedTotalHits();
        int totalPages = (int) Math.ceil((double) totalHits / request.getSize());

        SearchMetadata metadata = SearchMetadata.builder()
                .query(request.getKeyword() != null ? request.getKeyword() : "")
                .totalHits(totalHits)
                .page(request.getPage())
                .size(request.getSize())
                .totalPages(totalPages)
                .processingTimeMs(meilisearchResult.getProcessingTimeMs())
                .build();

        return PostSearchResponse.builder()
                .items(items)
                .metadata(metadata)
                .build();
    }

    /**
     * Meilisearch Hit를 PostSearchItem으로 변환
     */
    private PostSearchItem convertToSearchItem(Object hit) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> hitMap = (Map<String, Object>) hit;

            // _formatted에서 하이라이트된 content 추출
            @SuppressWarnings("unchecked")
            Map<String, Object> formatted = (Map<String, Object>) hitMap.get("_formatted");
            String highlightedContent = formatted != null ?
                    (String) formatted.get("content") :
                    (String) hitMap.get("content");

            return PostSearchItem.builder()
                    .id(getLongValue(hitMap.get("id")))
                    .mmMessageId((String) hitMap.get("mmMessageId"))
                    .mmChannelId((String) hitMap.get("mmChannelId"))
                    .userName((String) hitMap.get("userName"))
                    .content((String) hitMap.get("content"))
                    .highlightedContent(highlightedContent)
                    .mmCreatedAt(getLongValue(hitMap.get("mmCreatedAt")))
                    .mainCategory(getLongValue(hitMap.get("mainCategory")))
                    .subCategory(getLongValue(hitMap.get("subCategory")))
                    .build();
        } catch (Exception e) {
            log.error("Failed to convert search item", e);
            throw new RuntimeException("Failed to convert search item", e);
        }
    }

    /**
     * Object를 Long으로 안전하게 변환
     */
    private Long getLongValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private String buildFilter(PostSearchRequest request) {
        List<String> filters = new ArrayList<>();

        if (request.getChannelId() != null && !request.getChannelId().isBlank()) {
            filters.add("mmChannelId = '" + escapeFilterValue(request.getChannelId()) + "'");
        }

        if (request.getSubCategory() != null) {
            filters.add("subCategory = '" + request.getSubCategory() + "'");
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