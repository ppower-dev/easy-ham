package com.A105.prham.notice.domain;

import com.A105.prham.channel.domain.Channel;
import com.A105.prham.common.domain.BaseTimeEntity;
import com.A105.prham.maincode.domain.Maincode;
import com.A105.prham.post.domain.Post;
import com.A105.prham.subcode.domain.Subcode;
import com.A105.prham.team.domain.Team;
import com.A105.prham.user_notice_like.domain.UserNoticeLike;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "notices")
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

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

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

}
