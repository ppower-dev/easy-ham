package com.A105.prham.user.entity;

import com.A105.prham.auth.entity.RefreshToken;
import com.A105.prham.campus.entity.Campus;
import com.A105.prham.common.domain.BaseTimeEntity;
import com.A105.prham.common.entity.Notification;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Notification> notifications = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserSkill> userSkills = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserPosition> userPositions = new HashSet<>();

    @Builder
    public User(String name, Integer generation, Integer classroom, String email, Campus campus) {
        this.name = name;
        this.generation = generation;
        this.classroom = classroom;
        this.email = email;
        this.campus = campus;
    }
}
