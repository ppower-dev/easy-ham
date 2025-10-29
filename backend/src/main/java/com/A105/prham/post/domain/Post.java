package com.A105.prham.post.domain;

import com.A105.prham.common.domain.BaseTimeEntity;
import com.A105.prham.user_notice_like.domain.UserNoticeLike;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "posts")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<UserNoticeLike> userNoticeLikes;
}
