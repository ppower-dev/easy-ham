package com.A105.prham.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserSignupRequest {
    private String name;
    private Integer classroom;
    private Integer generation;
    private String email;
    private Long campusId;

    private List<Long> positionIds; // 선택: 희망 포지션
    private List<Long> skillIds;    // 선택: 보유 스킬
}
