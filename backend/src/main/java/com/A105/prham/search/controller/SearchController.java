//package com.A105.prham.search.controller;
//
//import com.A105.prham.search.dto.MessageDocument;
//import com.A105.prham.search.service.ElasticsearchService;
//import com.A105.prham.search.dto.SearchResponseDTO;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.http.ResponseEntity;
//import lombok.extern.slf4j.Slf4j;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/search")
//@Slf4j
//@CrossOrigin(origins = "*") // CORS 설정
//public class SearchController {
//
//    private final ElasticsearchService elasticsearchService;
//
//    public SearchController(ElasticsearchService elasticsearchService) {
//        this.elasticsearchService = elasticsearchService;
//    }
//
//    // 키워드 검색
//    @GetMapping
//    public ResponseEntity<List<SearchResponseDTO>> searchMessages(
//            @RequestParam String keyword,
//            @RequestParam(defaultValue = "10") int size) {
//
//        try {
//            log.info("Search request for keyword: '{}', size: {}", keyword, size);
//
//            List<MessageDocument> documents = elasticsearchService.searchByKeyword(keyword);
//
//            List<SearchResponseDTO> results = documents.stream()
//                    .limit(size)
//                    .map(SearchResponseDTO::from)
//                    .collect(Collectors.toList());
//
//            log.info("Found {} messages for keyword: '{}'", results.size(), keyword);
//
//            return ResponseEntity.ok(results);
//
//        } catch (Exception e) {
//            log.error("Search failed for keyword: '{}'", keyword, e);
//            return ResponseEntity.internalServerError().build();
//        }
//    }
//
//    // 채널별 검색
//    @GetMapping("/channel/{channelId}")
//    public ResponseEntity<List<SearchResponseDTO>> searchByChannel(
//            @PathVariable String channelId,
//            @RequestParam(defaultValue = "20") int size) {
//
//        try {
//            log.info("Channel search request: {}", channelId);
//
//            List<MessageDocument> documents = elasticsearchService.findByChannel(channelId);
//
//            List<SearchResponseDTO> results = documents.stream()
//                    .limit(size)
//                    .map(SearchResponseDTO::from)
//                    .collect(Collectors.toList());
//
//            return ResponseEntity.ok(results);
//
//        } catch (Exception e) {
//            log.error("Channel search failed: {}", channelId, e);
//            return ResponseEntity.internalServerError().build();
//        }
//    }
//
//    // 마감일이 있는 메시지들
//    @GetMapping("/deadlines")
//    public ResponseEntity<List<SearchResponseDTO>> getUpcomingDeadlines(
//            @RequestParam(defaultValue = "10") int size) {
//
//        try {
//            log.info("Upcoming deadlines request, size: {}", size);
//
//            List<MessageDocument> documents = elasticsearchService.findUpcomingDeadlines();
//
//            List<SearchResponseDTO> results = documents.stream()
//                    .limit(size)
//                    .map(SearchResponseDTO::from)
//                    .collect(Collectors.toList());
//
//            return ResponseEntity.ok(results);
//
//        } catch (Exception e) {
//            log.error("Deadlines search failed", e);
//            return ResponseEntity.internalServerError().build();
//        }
//    }
//
//    // 전체 텍스트 검색 (고급)
//    @GetMapping("/text")
//    public ResponseEntity<List<SearchResponseDTO>> searchByText(
//            @RequestParam String query,
//            @RequestParam(defaultValue = "10") int size) {
//
//        try {
//            log.info("Full text search: '{}', size: {}", query, size);
//
//            List<MessageDocument> documents = elasticsearchService.searchByText(query);
//
//            List<SearchResponseDTO> results = documents.stream()
//                    .limit(size)
//                    .map(SearchResponseDTO::from)
//                    .collect(Collectors.toList());
//
//            return ResponseEntity.ok(results);
//
//        } catch (Exception e) {
//            log.error("Text search failed: '{}'", query, e);
//            return ResponseEntity.internalServerError().build();
//        }
//    }
//}