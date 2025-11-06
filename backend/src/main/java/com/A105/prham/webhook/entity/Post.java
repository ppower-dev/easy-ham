package com.A105.prham.webhook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.A105.prham.common.domain.BaseTimeEntity;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "post_id", nullable = false, unique = true)
	private String postId;

	@Column(name = "channel_id", nullable = false)
	private String channelId;

	@Column(name = "user_id", nullable = false)
	private String userId;

	@Column(name = "webhook_timestamp") // 💡 원본 DTO의 timestamp 필드
	private String webhookTimestamp;

	@Column(name = "original_text", columnDefinition = "TEXT")
	private String originalText;

	@Column(name = "file_ids") // ✨ 파일 ID 필드 추가
	private String fileIds;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<File> files = new ArrayList<>();

	// --- 비동기 처리로 채워질 필드들 ---
	@Column(name = "cleaned_text", columnDefinition = "TEXT")
	private String cleanedText;

	@Column(name = "deadline")
	private String deadline;

	@Column(name = "category") // ✨ LLM 분류 결과
	private String category;

	@Column(name = "storage_file_urls", columnDefinition = "TEXT") // ✨ S3 등 저장된 URL
	private String storageFileUrls;

	@Enumerated(EnumType.STRING) // ✨ 처리 상태
	@Column(name = "status", nullable = false)
	private PostStatus status;

	@Column(name = "processed_at") // 처리 완료 시간
	private String processedAt;

	@Column(name = "created_at", nullable = false)
	private String createdAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now().toString();
		if (status == null) {
			status = PostStatus.PENDING; // ✨ 기본 상태는 PENDING
		}
	}

	public void addFile(File file) {
		files.add(file);
		file.setPost(this); // File 엔티티의 setPost 메서드 호출
	}
}