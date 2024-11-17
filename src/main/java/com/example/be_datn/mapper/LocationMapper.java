package com.example.be_datn.mapper;

import com.example.be_datn.dto.Response.DistrictResponse;
import com.example.be_datn.dto.Response.ProvinceResponse;
import com.example.be_datn.dto.Response.WardResponse;
import com.example.be_datn.entity.District;
import com.example.be_datn.entity.Province;
import com.example.be_datn.entity.Ward;
import org.springframework.stereotype.Component;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Component
public class LocationMapper {
    public ProvinceResponse toResponse(Province province) {
        return ProvinceResponse.builder()
                .code(province.getCode())
                .fullName(province.getFullName())
                .latitude(province.getLatitude())
                .longitude(province.getLongitude())
                .build();
    }

    public List<ProvinceResponse> toListProvicneResponse(List<Province> provinces) {
        return provinces.stream().map(this::toResponse).toList();
    }

    public DistrictResponse toResponse(District district) {
        return DistrictResponse.builder()
                .code(district.getCode())
                .fullName(district.getFullName())
                .build();
    }

    public List<DistrictResponse> toListDistrictResponse(List<District> districts) {
        return districts.stream().map(this::toResponse).toList();
    }

    public WardResponse toResponse(Ward ward) {
        return WardResponse.builder()
                .code(ward.getCode())
                .fullName(ward.getFullName())
                .build();
    }

    public List<WardResponse> toListWardResponse(List<Ward> wards) {
        return wards.stream().map(this::toResponse).toList();
    }
}
