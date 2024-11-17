package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Response.DistrictResponse;
import com.example.be_datn.dto.Response.ProvinceResponse;
import com.example.be_datn.dto.Response.WardResponse;
import com.example.be_datn.mapper.LocationMapper;
import com.example.be_datn.repository.DistrictRepository;
import com.example.be_datn.repository.ProvinceRepository;
import com.example.be_datn.repository.WardRepository;
import com.example.be_datn.service.ILocationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocationService implements ILocationService {
    LocationMapper locationMapper;
    ProvinceRepository provinceRepository;
    DistrictRepository districtRepository;
    WardRepository wardRepository;

    @Override
    public List<ProvinceResponse> findAllProvince() {
        return locationMapper.toListProvicneResponse(provinceRepository.findAll());
    }

    @Override
    public List<DistrictResponse> findAllDistrict(String codeProvince) {
        return locationMapper.toListDistrictResponse(districtRepository.findByProvince_Code(codeProvince));
    }

    @Override
    public List<WardResponse> findAllWard(String codeDistrict) {
        return locationMapper.toListWardResponse(wardRepository.findByDistrict_Code(codeDistrict));
    }
}
