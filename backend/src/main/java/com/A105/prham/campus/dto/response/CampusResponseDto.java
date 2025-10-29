package com.A105.prham.campus.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CampusResponseDto {
    private Long id;
    private String name;
}
