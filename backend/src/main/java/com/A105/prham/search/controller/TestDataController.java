package com.A105.prham.search.controller;

import com.A105.prham.search.dto.request.CreateTestPostRequest;
import com.A105.prham.search.service.SearchService;
import com.A105.prham.webhook.entity.Post;
import com.A105.prham.webhook.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/test/search")
@RequiredArgsConstructor
public class TestDataController {

    private final PostRepository postRepository;
    private final SearchService searchService;

    /**
     * 테스트용 Post 데이터 생성 및 자동 인덱싱
     * Webhook 구현 전까지 임시로 사용
     */
    @PostMapping("/posts")
    public ResponseEntity<Map<String, Object>> createTestPost(@RequestBody CreateTestPostRequest request) {
        try {
            log.info("Creating test post: {}", request);

            // 1. DB에 Post 저장 (Builder 패턴 사용)
            Post post = Post.builder()
                    .mmMessageId(request.getMmMessageId())
                    .mmTeamId(request.getMmTeamId() != null ? request.getMmTeamId() : "default_team")
                    .mmUserId(request.getMmUserId() != null ? request.getMmUserId() : "test_user")
                    .userName(request.getUserName())
                    .mmChannelId(request.getMmChannelId())
                    .mmCreatedAt(request.getMmCreatedAt() != null ? request.getMmCreatedAt() : System.currentTimeMillis())
                    .content(request.getContent())
                    .build();

            Post savedPost = postRepository.save(post);
            log.info("Post saved to DB with ID: {}", savedPost.getId());

            // 2. Meilisearch에 자동 인덱싱
            searchService.indexPostWithCategories(
                    savedPost,
                    request.getMainCategory(),
                    request.getSubCategory()
            );
            log.info("Post indexed to Meilisearch: {}", savedPost.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("postId", savedPost.getId());
            response.put("message", "Test post created and indexed successfully");
            response.put("data", savedPost);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Failed to create test post", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 여러 테스트 Post 한번에 생성
     */
    @PostMapping("/posts/bulk")
    public ResponseEntity<Map<String, Object>> createTestPostsBulk(@RequestBody CreateTestPostRequest[] requests) {
        try {
            log.info("Creating {} test posts", requests.length);

            int successCount = 0;
            for (CreateTestPostRequest request : requests) {
                try {
                    Post post = Post.builder()
                            .mmMessageId(request.getMmMessageId())
                            .mmTeamId(request.getMmTeamId() != null ? request.getMmTeamId() : "default_team")
                            .mmUserId(request.getMmUserId() != null ? request.getMmUserId() : "test_user")
                            .userName(request.getUserName())
                            .mmChannelId(request.getMmChannelId())
                            .mmCreatedAt(request.getMmCreatedAt() != null ? request.getMmCreatedAt() : System.currentTimeMillis())
                            .content(request.getContent())
                            .build();

                    Post savedPost = postRepository.save(post);
                    searchService.indexPostWithCategories(
                            savedPost,
                            request.getMainCategory(),
                            request.getSubCategory()
                    );
                    successCount++;
                } catch (Exception e) {
                    log.error("Failed to create post: {}", request, e);
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("totalRequests", requests.length);
            response.put("successCount", successCount);
            response.put("failedCount", requests.length - successCount);
            response.put("message", String.format("Created and indexed %d/%d posts", successCount, requests.length));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Failed to create bulk test posts", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 테스트 Post 삭제 (DB + Meilisearch)
     */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Map<String, Object>> deleteTestPost(@PathVariable Long postId) {
        try {
            log.info("Deleting test post: {}", postId);

            // 1. DB에서 삭제
            postRepository.deleteById(postId);
            log.info("Post deleted from DB: {}", postId);

            // 2. Meilisearch에서 삭제
            searchService.deletePost(postId);
            log.info("Post deleted from Meilisearch: {}", postId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("postId", postId);
            response.put("message", "Test post deleted successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Failed to delete test post: {}", postId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 모든 테스트 데이터 삭제 (주의: DB의 모든 Post + Meilisearch 인덱스 삭제)
     */
    @DeleteMapping("/posts/all")
    public ResponseEntity<Map<String, Object>> deleteAllTestPosts() {
        try {
            log.warn("Deleting ALL posts from DB and Meilisearch");

            long count = postRepository.count();

            // 1. DB에서 모든 Post 삭제
            postRepository.deleteAll();
            log.info("✅ Deleted {} posts from DB", count);

            // 2. Meilisearch에서 모든 문서 삭제
            searchService.deleteAllDocuments();
            log.info("✅ Deleted all documents from Meilisearch");

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("deletedCount", count);
            response.put("message", String.format("Successfully deleted %d posts from both DB and Meilisearch", count));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Failed to delete all test posts", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}