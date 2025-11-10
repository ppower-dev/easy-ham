package com.A105.prham.campus.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CampusResponse {
    List<CampusResponseDto> campuses;
}
