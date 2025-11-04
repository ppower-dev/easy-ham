package com.A105.prham.auth.entity;

import com.A105.prham.common.domain.BaseTimeEntity;
import com.A105.prham.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_tokens")
public class RefreshToken extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(nullable = false, length = 512)
    private String token;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    private Boolean revoked;

    @Builder
    public RefreshToken(User user, String token, LocalDateTime expiresAt, Boolean revoked) {
        this.user = user;
        this.token = token;
        this.expiresAt = expiresAt;
        this.revoked = revoked;
    }
}
