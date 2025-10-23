package com.mmA.mymm.messages.repository;

import com.mmA.mymm.messages.dto.MessageDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageDocumentRepository extends ElasticsearchRepository<MessageDocument, String> {

    // 텍스트 검색 (cleaned_text 필드에서)
    List<MessageDocument> findByCleanedTextContaining(String keyword);

    // 채널별 검색
    List<MessageDocument> findByChannelId(String channelId);

    // 마감일이 있는 메시지들
    List<MessageDocument> findByDeadlineIsNotNull();

    // 복합 검색 쿼리
    @Query("""
    {
      "match": {
        "cleanedText": {
          "query": "?0",
          "analyzer": "my_synonym_analyzer"
        }
      }
    }
    """)
    List<MessageDocument> searchByText(String text);

    // 날짜 범위 검색
    List<MessageDocument> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}