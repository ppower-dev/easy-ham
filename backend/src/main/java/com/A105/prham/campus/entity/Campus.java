package com.A105.prham.campus.entity;

import com.A105.prham.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "campus")
public class Campus extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campus_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Builder
    public Campus(String name) {
        this.name = name;
    }
}