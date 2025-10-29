package com.A105.prham.skill.dto.response;

import com.A105.prham.campus.dto.response.CampusResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class SkillResponse {
    List<SkillResponseDto> skills;
}
