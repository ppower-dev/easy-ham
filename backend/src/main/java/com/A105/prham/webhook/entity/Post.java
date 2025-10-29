package com.A105.prham.webhook.entity;

import java.util.ArrayList;
import java.util.List;

import com.A105.prham.notice.entity.Notice;
import com.A105.prham.common.domain.BaseTimeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//인덱싱
//1. mmMessageId 기준으로 인덱스
//2. mmChannelId 기준으로 인덱스
@Table(name = "posts",
	indexes = {
	@Index(name = "idx_mm_message_id", columnList = "mmMessageId", unique = true),
	@Index(name = "idx_mm_channel_id", columnList = "mmChannelId")
})
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	@Column(name = "mm_message_id", nullable = false, unique = true)
	private String mmMessageId;

	@Column(name = "mm_team_id", nullable = false)
	private String mmTeamId;

	@Column(name = "mm_user_id", nullable = false)
	private String mmUserId;

	@Column(name = "mm_user_name", nullable = false)
	private String userName;

	@Column(name = "mm_channel_id", nullable = false)
	private String mmChannelId;

	@Column(name = "mm_created_at", nullable = false)
	private Long mmCreatedAt;

	@Column(name = "content", columnDefinition = "TEXT", nullable = false)
	private String content;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<File> files = new ArrayList<>();

	@OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private Notice notice;

	@Builder
	public Post(String mmMessageId, String mmTeamId, String mmUserId, String userName, String mmChannelId, Long mmCreatedAt, String content) {
		this.mmMessageId = mmMessageId;
		this.mmTeamId = mmTeamId;
		this.mmUserId = mmUserId;
		this.userName = userName;
		this.mmChannelId = mmChannelId;
		this.mmCreatedAt = mmCreatedAt;
		this.content = content;
	}

	public void addFile(File file) {
		this.files.add(file);
		file.setPost(this);
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
		notice.setPost(this);
	}
}
