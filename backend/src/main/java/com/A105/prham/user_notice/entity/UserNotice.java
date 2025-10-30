package com.A105.prham.user_notice.entity;

import com.A105.prham.channel.entity.Channel;
import com.A105.prham.common.domain.BaseTimeEntity;
import com.A105.prham.common.domain.Maincode;
import com.A105.prham.common.domain.Subcode;
import com.A105.prham.notice.entity.Notice;
import com.A105.prham.team.entity.Team;
import com.A105.prham.user.entity.User;
import com.A105.prham.user_notice_like.entity.UserNoticeLike;
import com.A105.prham.webhook.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_notices")
public class UserNotice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_notice_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_id")
    private Subcode subcode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upper_code_id")
    private Maincode maincode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted;

    @OneToMany(mappedBy = "usernotice", fetch = FetchType.LAZY)
    private List<UserNoticeLike> userNoticeLikes = new ArrayList<>();

    @Builder
    public UserNotice(User user, Notice notice, Subcode subcode, Maincode maincode, Channel channel, Team team, Post post, Boolean isCompleted) {
        this.user = user;
        this.notice = notice;
        this.subcode = subcode;
        this.maincode = maincode;
        this.channel = channel;
        this.team = team;
        this.post = post;
        this.isCompleted = isCompleted;
    }
}
