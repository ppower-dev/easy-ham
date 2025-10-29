package com.A105.prham.position.entity;



import com.A105.prham.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "position")
public class Position extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String positionName;

    @Builder
    public Position(String positionName) {
        this.positionName = positionName;
    }
}
