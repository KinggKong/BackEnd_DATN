package com.example.be_datn.mapper;

import com.example.be_datn.dto.request.DanhMucCreationRequest;
import com.example.be_datn.dto.request.DanhMucUpdateRequest;
import com.example.be_datn.dto.response.DanhMucResponse;
import com.example.be_datn.entity.DanhMuc;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DanhMucMapper {
    DanhMuc toDanhMuc(DanhMucCreationRequest request);
    DanhMucResponse toDanhMucResponse(DanhMuc danhMuc);
    void updateDanhMuc(@MappingTarget DanhMuc danhMuc, DanhMucUpdateRequest request);
    List<DanhMucResponse> toListThuongHieuResponse(List<DanhMuc> danhMucs);
}
