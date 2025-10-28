package com.A105.prham.post.entity;

import java.time.OffsetDateTime;

import com.A105.prham.common.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//인덱싱
//1. mmMessageId 기준으로 인덱스
//2. mmChannelId 기준으로 인덱스
@Table(indexes = {
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

	@Column(nullable = false, unique = true)
	private String mmMessageId;

	@Column(nullable = false)
	private String mmTeamId;

	@Column(nullable = false)
	private String mmUserId;

	@Column(nullable = false)
	private String userName;

	@Column(nullable = false)
	private String mmChannelId;

	@Column(nullable = false)
	private Long mmCreatedAt;

	@Column(nullable = false)
	private String content;

	@Builder
	public Post(String mmMessageId, String mmTeamId, String mmChannelId, String mmUserId, String userName, String content, Long mmCreatedAt) {
		this.mmMessageId = mmMessageId;
		this.mmTeamId = mmTeamId;
		this.mmChannelId = mmChannelId;
		this.mmUserId = mmUserId;
		this.userName = userName;
		this.content = content;
		this.mmCreatedAt = mmCreatedAt;
	}
}
