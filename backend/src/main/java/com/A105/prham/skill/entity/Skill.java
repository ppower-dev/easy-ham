package com.A105.prham.skill.entity;

import com.A105.prham.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "skill")
public class Skill extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String skillName;

    @Builder
    public Skill(String skillName) {
        this.skillName = skillName;
    }
}
