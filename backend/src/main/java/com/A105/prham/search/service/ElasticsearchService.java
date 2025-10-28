//package com.A105.prham.search.service;
//
//import com.A105.prham.search.dto.MessageDocument;
//import com.A105.prham.messages.dto.ProcessedMessage;
//import com.A105.prham.search.repository.MessageDocumentRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import lombok.extern.slf4j.Slf4j;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class ElasticsearchService {
//
//    private final MessageDocumentRepository documentRepository;
//
//    public void saveMessage(ProcessedMessage processedMessage) {
//        try {
//            // ProcessedMessage -> MessageDocument 변환
//            MessageDocument document = convertToDocument(processedMessage);
//
//            // Elasticsearch에 저장
//            documentRepository.save(document);
//            log.info("Message saved to Elasticsearch: {}", processedMessage.getPostId());
//
//        } catch (Exception e) {
//            log.error("Failed to save message to Elasticsearch: {}", processedMessage.getPostId(), e);
//            throw new RuntimeException("Elasticsearch save failed", e);
//        }
//    }
//
//    private MessageDocument convertToDocument(ProcessedMessage processed) {
//        MessageDocument document = new MessageDocument();
//        document.setPostId(processed.getPostId());
//        document.setChannelId(processed.getChannelId());
//        document.setUserId(processed.getUserId());
//        document.setOriginalText(processed.getOriginalText());
//        document.setCleanedText(processed.getCleanedText());
//
//        // LocalDateTime을 String으로 변환
//        document.setDeadline(processed.getDeadline() != null ? processed.getDeadline().toString() : null);
//        document.setCreatedAt(LocalDateTime.now().toString());
//
//        return document;
//    }
//    // 검색 메서드들
//    public List<MessageDocument> searchByKeyword(String keyword) {
//        return documentRepository.findByCleanedTextContaining(keyword);
//    }
//
//    public List<MessageDocument> searchByText(String text) {
//        return documentRepository.searchByText(text);
//    }
//
//    public List<MessageDocument> findByChannel(String channelId) {
//        return documentRepository.findByChannelId(channelId);
//    }
//
//    public List<MessageDocument> findUpcomingDeadlines() {
//        return documentRepository.findByDeadlineIsNotNull();
//    }
//}
