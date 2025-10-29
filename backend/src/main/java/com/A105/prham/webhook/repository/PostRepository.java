package com.A105.prham.webhook.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.A105.prham.webhook.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	// mattermost 메시지 id로 조회
	//중복 체크
	Optional<Post> findByMmMessageId(String mmMessageId);

	// 메시지 존재하는가
	boolean existsByMmMessageId(String mmMessageId);

	//post와 file 함께 조회
	@Query("SELECT p FROM Post p LEFT JOIN FETCH p.files WHERE p.id = :id")
	Optional<Post> findByIdWithFiles(Long id);

	//post와 notice를 함께 조회
	@Query("SELECT p FROM Post p LEFT JOIN FETCH p.notice WHERE p.id = :id")
	Optional<Post> findByIdWithNotice(Long id);
}
