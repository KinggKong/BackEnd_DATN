package com.example.be_datn.controller;

import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.repository.ProvinceRepository;
import com.example.be_datn.service.ILocationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/locations")
public class LocationController {
    ILocationService locationService;


    @GetMapping("/provinces")
    public ApiResponse<?> findAllProvinces() {
        return ApiResponse.builder()
                .data(locationService.findAllProvince())
                .message("find all provinces successfull")
                .build();
    }

    @GetMapping("/districts")
    public ApiResponse<?> findAllDistricts(@RequestParam(required = false) String provinceCode) {
        return ApiResponse.builder()
                .data(locationService.findAllDistrict(provinceCode))
                .message("find all district successfull")
                .build();
    }

    @GetMapping("/wards")
    public ApiResponse<?> findAllWard(@RequestParam(required = false) String districtCode) {
        return ApiResponse.builder()
                .data(locationService.findAllWard(districtCode))
                .message("find all provinces successfull")
                .build();
    }
}
