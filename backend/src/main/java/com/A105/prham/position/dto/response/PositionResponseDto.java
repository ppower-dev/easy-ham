package com.A105.prham.position.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PositionResponseDto {
    private Long id;
    private String name;
}
