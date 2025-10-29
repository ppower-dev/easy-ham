package com.A105.prham.notice.entity;

import com.A105.prham.channel.domain.Channel;
import com.A105.prham.common.domain.BaseTimeEntity;
import com.A105.prham.maincode.domain.Maincode;
import com.A105.prham.subcode.domain.Subcode;
import com.A105.prham.team.domain.Team;
import com.A105.prham.user_notice_like.domain.UserNoticeLike;
import com.A105.prham.webhook.entity.Post;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "notices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notice_id")
	private Long id;

	@OneToOne
	@JoinColumn(name = "code_id")
	private Subcode subcode;

	@OneToOne
	@JoinColumn(name = "upper_code_id")
	private Maincode maincode;

	@ManyToOne
	@JoinColumn(name = "team_id")
	private Team team;

	@ManyToOne
	@JoinColumn(name = "channel_id")
	private Channel channel;

	@OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
	private List<UserNoticeLike> userNoticeLikes;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content")
	private String content;

	@Column(name = "author_id", nullable = false)
	private String authorId;

	@Column(name = "deadline")
	private String deadline;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false, updatable = false)
	private Post post;

	public void setPost(Post post) {
		this.post = post;
	}


}
