package com.A105.prham.common.entity;

import com.A105.prham.common.domain.BaseTimeEntity;
import com.A105.prham.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notification")
public class Notification extends BaseTimeEntity {
    //나중에 해당 도메인으로 이전하세용
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Boolean isRead;

    @Column(nullable = false, length = 500)
    private String content;

    @Builder
    public Notification(User user, String type, Boolean isRead, String content) {
        this.user = user;
        this.type = type;
        this.isRead = isRead;
        this.content = content;
    }
}
