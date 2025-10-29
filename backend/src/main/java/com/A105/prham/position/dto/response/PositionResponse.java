package com.A105.prham.position.dto.response;

import com.A105.prham.campus.dto.response.CampusResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PositionResponse {
    List<PositionResponseDto> positions;
}
