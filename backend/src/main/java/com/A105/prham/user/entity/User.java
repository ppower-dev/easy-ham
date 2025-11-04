package com.A105.prham.user.entity;

import com.A105.prham.auth.entity.RefreshToken;
import com.A105.prham.campus.entity.Campus;
import com.A105.prham.common.domain.BaseTimeEntity;
import com.A105.prham.keyword.Keyword;
import com.A105.prham.user_notice.entity.UserNotice;
import com.A105.prham.user_notice_like.entity.UserNoticeLike;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer classroom;

    @Column(nullable = false)
    private Integer generation;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_id")
    private Campus campus;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RefreshToken refreshToken;

//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Notification> notifications = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserSkill> userSkills = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserPosition> userPositions = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserNotice> userNotices = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserNoticeLike> userNoticeLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Keyword> keywords = new ArrayList<>();

    @Builder
    public User(String name, Integer generation, Integer classroom, String email, Campus campus) {
        this.name = name;
        this.generation = generation;
        this.classroom = classroom;
        this.email = email;
        this.campus = campus;
    }
}
