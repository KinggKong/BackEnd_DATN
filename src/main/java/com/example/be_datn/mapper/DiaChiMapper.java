package com.example.be_datn.mapper;

import com.example.be_datn.dto.Request.diaChi.DiaChiCreationRequest;
import com.example.be_datn.dto.Request.diaChi.DiaChiUpdateRequest;
import com.example.be_datn.dto.Response.DiaChiResponse;
import com.example.be_datn.entity.DiaChi;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiaChiMapper {
    DiaChi toDiaChi(DiaChiCreationRequest request);
    DiaChiResponse toDiaChiResponse(DiaChi diaChi);
    void updateDiaChi(@MappingTarget DiaChi diaChi, DiaChiUpdateRequest request);
    List<DiaChiResponse> toListDiaChiResponse(List<DiaChi> diaChi);
}
