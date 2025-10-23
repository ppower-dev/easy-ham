package com.mmA.mymm.messages.repository;

import com.mmA.mymm.messages.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // 🔹 채널별 메시지 조회
    List<Message> findByChannelIdOrderByCreatedAtDesc(String channelId);

    // 🔹 날짜 범위로 조회 (String 비교)
    // ISO-8601 형태("2025-10-22T10:30:00")라면 문자열 비교도 정상 작동
    @Query("""
        SELECT m FROM Message m
        WHERE m.createdAt BETWEEN :start AND :end
        ORDER BY m.createdAt DESC
        """)
    List<Message> findByCreatedAtBetweenOrderByCreatedAtDesc(
            @Param("start") String start,
            @Param("end") String end
    );

    // 🔹 마감일이 있는 메시지들 (deadline도 String이라면 그대로 두기)
    List<Message> findByDeadlineIsNotNullOrderByDeadlineAsc();

    // 🔹 텍스트 검색 (간단한 LIKE 검색)
    List<Message> findByCleanedTextContainingIgnoreCaseOrderByCreatedAtDesc(String keyword);

    // 🔹 Mattermost post ID로 중복 체크
    boolean existsByPostId(String postId);

    // 🔹 Mattermost post ID로 조회
    Optional<Message> findByPostId(String postId);
}
