package com.example.be_datn.service;

import com.example.be_datn.dto.Response.DistrictResponse;
import com.example.be_datn.dto.Response.ProvinceResponse;
import com.example.be_datn.dto.Response.WardResponse;

import java.util.List;

public interface ILocationService {
    List<ProvinceResponse> findAllProvince();

    List<DistrictResponse> findAllDistrict(String codeProvince);

    List<WardResponse> findAllWard(String codeDistrict);
}
