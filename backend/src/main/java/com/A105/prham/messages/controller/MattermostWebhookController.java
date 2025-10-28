package com.A105.prham.messages.controller;

import com.A105.prham.messages.dto.MattermostWebhookDTO;
import com.A105.prham.messages.dto.ProcessedMessage;
//import com.A105.prham.search.service.ElasticsearchService;
import com.A105.prham.messages.service.MessageProcessorService;
import com.A105.prham.messages.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/webhook")
@Slf4j
@RequiredArgsConstructor
public class MattermostWebhookController {

    private final MessageProcessorService messagePreprocessor;
    private final MessageService messageService;
//    private final   ElasticsearchService elasticsearchService;
    @PostMapping("/mattermost")
    public ResponseEntity<String> receiveMattermostMessage(
            @RequestBody MattermostWebhookDTO payload) {

        try {
            log.info("Received message from channel: {}, user: {}",
                    payload.getChannelId(), payload.getUserName());

            // 1. 전처리
            ProcessedMessage processed = messagePreprocessor.preprocess(payload);

            // 2. DB 저장
            messageService.saveMessage(processed);

            // 3. Elasticsearch 저장
//            elasticsearchService.saveMessage(processed);

            return ResponseEntity.ok("Message processed and saved");

        } catch (Exception e) {
            log.error("Error processing webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing message");
        }
    }
}