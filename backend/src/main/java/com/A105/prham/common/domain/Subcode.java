package com.A105.prham.common.domain;

import com.A105.prham.common.domain.Maincode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "subcode")
public class Subcode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
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
}
