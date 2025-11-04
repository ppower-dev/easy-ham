package com.A105.prham.common.domain;

import com.A105.prham.common.domain.Maincode;
import com.A105.prham.user_notice.entity.UserNotice;
import com.A105.prham.user_notice_like.entity.UserNoticeLike;
import com.A105.prham.webhook.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "subcode")
public class Subcode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "upper_code_id")
    private Maincode maincode;

    @Column(name = "sub_code", nullable = false, length = 10)
    private String subcode;

    @Column(name = "sub_code_name", nullable = false, length = 100)
    private String subcodeName;

    @Column(name = "sub_code_description", nullable = false, length = 100)
    private String subcodeDescription;

    @Column(name = "is_used")
    private Boolean isUsed;

    @OneToMany(mappedBy = "subcode", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserNoticeLike> userNoticeLikes = new ArrayList<>();

    @OneToMany(mappedBy = "subcode", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserNotice> userNotices = new ArrayList<>();
}
