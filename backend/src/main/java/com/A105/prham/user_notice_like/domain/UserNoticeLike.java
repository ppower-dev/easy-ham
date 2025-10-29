package com.A105.prham.user_notice_like.domain;

import com.A105.prham.channel.domain.Channel;
import com.A105.prham.common.domain.BaseTimeEntity;
import com.A105.prham.maincode.domain.Maincode;
import com.A105.prham.notice.domain.Notice;
import com.A105.prham.post.domain.Post;
import com.A105.prham.subcode.domain.Subcode;
import com.A105.prham.team.domain.Team;
import com.A105.prham.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_notice_likes")
public class UserNoticeLike extends BaseTimeEntity { //북마크

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_notice_like_id")
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "notice_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Notice notice;

    @JoinColumn(name = "team_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @JoinColumn(name = "code_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Subcode code;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upper_code_id")
    private Maincode maincode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private Boolean isLiked;

    @Builder
    public UserNoticeLike(User user, Notice notice, Team team, Subcode code, Maincode maincode, Channel channel, Post post, Boolean isLiked) {
        this.user = user;
        this.notice = notice;
        this.team = team;
        this.code = code;
        this.maincode = maincode;
        this.channel = channel;
        this.post = post;
        this.isLiked = isLiked;
    }
}
