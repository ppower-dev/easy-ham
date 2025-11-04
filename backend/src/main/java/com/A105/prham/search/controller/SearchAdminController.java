package com.A105.prham.search.controller;

import com.A105.prham.search.service.SearchService;
import com.A105.prham.webhook.entity.Post;
import com.A105.prham.webhook.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/search")
@RequiredArgsConstructor
public class SearchAdminController {

    private final PostRepository postRepository;
    private final SearchService searchService;

    // 전체 재색인
    @PostMapping("/reindex")
    public ResponseEntity<Map<String, Object>> reindexAll() {
        log.info("Starting full reindex...");

        List<Post> posts = postRepository.findAll();
        searchService.indexPosts(posts);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("indexedCount", posts.size());
        response.put("message", "Successfully reindexed all posts");

        log.info("Reindexed {} posts", posts.size());
        return ResponseEntity.ok(response);
    }

    // 색인 상태 확인
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getIndexStatus() {
        try {
            var stats = searchService.getIndexStats();

            Map<String, Object> response = new HashMap<>();
            response.put("stats", stats);
            response.put("success", true);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}