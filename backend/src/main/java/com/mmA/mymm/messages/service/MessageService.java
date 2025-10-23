package com.mmA.mymm.messages.service;

import com.mmA.mymm.messages.dto.ProcessedMessage;
import com.mmA.mymm.messages.entity.Message;
import com.mmA.mymm.messages.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage(ProcessedMessage processedMessage) {
        try {
            // 중복 체크
            if (messageRepository.existsByPostId(processedMessage.getPostId())) {
                log.info("Message already exists: {}", processedMessage.getPostId());
                return;
            }

            // ProcessedMessage -> Entity 변환
            Message entity = convertToEntity(processedMessage);

            // 저장
            messageRepository.save(entity);
            log.info("Message saved to database: {}", processedMessage.getPostId());

        } catch (Exception e) {
            log.error("Failed to save message: {}", processedMessage.getPostId(), e);
            throw new RuntimeException("Database save failed", e);
        }
    }

    private Message convertToEntity(ProcessedMessage processed) {
        Message entity = new Message();
        entity.setPostId(processed.getPostId());
        entity.setChannelId(processed.getChannelId());
        entity.setUserId(processed.getUserId());
        entity.setTimestamp(processed.getTimestamp());
        entity.setOriginalText(processed.getOriginalText());
        entity.setCleanedText(processed.getCleanedText());
        entity.setDeadline(processed.getDeadline());
        entity.setProcessedAt(processed.getProcessedAt());

        return entity;
    }

    // 검색 메서드들
    public List<Message> searchByKeyword(String keyword) {
        return messageRepository.findByCleanedTextContainingIgnoreCaseOrderByCreatedAtDesc(keyword);
    }

    public List<Message> findByChannel(String channelId) {
        return messageRepository.findByChannelIdOrderByCreatedAtDesc(channelId);
    }

    public List<Message> findUpcomingDeadlines() {
        return messageRepository.findByDeadlineIsNotNullOrderByDeadlineAsc();
    }
}