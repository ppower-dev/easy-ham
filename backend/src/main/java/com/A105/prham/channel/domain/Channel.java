package com.A105.prham.channel.domain;

import com.A105.prham.common.domain.BaseTimeEntity;
import com.A105.prham.notice.entity.Notice;
import com.A105.prham.team.domain.Team;
import com.A105.prham.user_notice_like.domain.UserNoticeLike;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "channels")
public class Channel extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    private Long id;

    @Column(name = "channel_name")
    private String channelName;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    private List<Notice> notices;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    private List<UserNoticeLike> userNoticeLikes;
}
