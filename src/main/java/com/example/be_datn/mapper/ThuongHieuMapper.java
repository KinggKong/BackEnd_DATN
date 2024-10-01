package com.example.be_datn.mapper;

import com.example.be_datn.dto.request.ThuongHieuCreationRequest;
import com.example.be_datn.dto.request.ThuongHieuUpdateRequest;
import com.example.be_datn.dto.response.ThuongHieuResponse;
import com.example.be_datn.entity.ThuongHieu;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ThuongHieuMapper {
    ThuongHieu toThuongHieu(ThuongHieuCreationRequest request);
    ThuongHieuResponse toThuongHieuResponse(ThuongHieu thuongHieu);
    void updateThuongHieu(@MappingTarget ThuongHieu thuongHieu, ThuongHieuUpdateRequest request);
    List<ThuongHieuResponse> toListThuongHieuResponse(List<ThuongHieu> thuongHieuList);
}
