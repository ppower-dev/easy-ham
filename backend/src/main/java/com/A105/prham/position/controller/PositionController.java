package com.A105.prham.position.controller;

import com.A105.prham.campus.service.CampusService;
import com.A105.prham.common.response.ApiResponseDto;
import com.A105.prham.common.response.ErrorCode;
import com.A105.prham.common.response.SuccessCode;
import com.A105.prham.position.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/position")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @GetMapping
    public ApiResponseDto<?> getAllPositionCode(){
        try{
            return ApiResponseDto.success(SuccessCode.SUCCESS,positionService.getAllPositions());
        } catch (Exception e) {
            return ApiResponseDto.fail(ErrorCode.INTERNAL_SERVER_ERROR);
        }


    }
}
