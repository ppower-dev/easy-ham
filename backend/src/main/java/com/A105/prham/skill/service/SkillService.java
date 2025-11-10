package com.A105.prham.skill.service;

import com.A105.prham.campus.dto.response.CampusResponse;
import com.A105.prham.campus.dto.response.CampusResponseDto;
import com.A105.prham.campus.repository.CampusRepository;
import com.A105.prham.skill.dto.response.SkillResponse;
import com.A105.prham.skill.dto.response.SkillResponseDto;
import com.A105.prham.skill.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillResponse getAllCampuses() {
        List<SkillResponseDto> skillList = skillRepository.findAll().stream()
                .map(skill -> SkillResponseDto.builder()
                        .id(skill.getId())
                        .name(skill.getSkillName())
                        .build())
                .collect(Collectors.toList());

        return SkillResponse.builder()
                .skills(skillList)
                .build();
    }
}