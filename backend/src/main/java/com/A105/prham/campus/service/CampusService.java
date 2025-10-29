package com.A105.prham.campus.service;

import com.A105.prham.campus.dto.response.CampusResponse;
import com.A105.prham.campus.dto.response.CampusResponseDto;
import com.A105.prham.campus.entity.Campus;
import com.A105.prham.campus.repository.CampusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampusService {

    private final CampusRepository campusRepository;

    public CampusResponse getAllCampuses() {
        List<CampusResponseDto> campusList = campusRepository.findAll().stream()
                .map(campus -> CampusResponseDto.builder()
                        .id(campus.getId())
                        .name(campus.getName())
                        .build())
                .collect(Collectors.toList());

        return CampusResponse.builder()
                .campuses(campusList)
                .build();
    }
}