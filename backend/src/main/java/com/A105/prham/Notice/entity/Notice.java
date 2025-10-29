package com.A105.prham.Notice.entity;

import com.A105.prham.common.domain.BaseTimeEntity;
import com.A105.prham.webhook.entity.Post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notice_id")
	private Long id;

	@Column(nullable = false)
	private String title;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false, updatable = false)
	private Post post;

	public void setPost(Post post) {
		this.post = post;
	}


}
