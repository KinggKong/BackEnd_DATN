package com.example.be_datn.mapper;

import com.example.be_datn.dto.Request.NhanVienRequest;
import com.example.be_datn.dto.Request.NhanVienUpdate;
import com.example.be_datn.dto.Response.NhanVienResponse;
import com.example.be_datn.entity.NhanVien;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NhanVienEntity2Response {
    NhanVienResponse to(NhanVien nhanVien);

    NhanVien toEntity(NhanVienRequest request);
    NhanVien toEntity(NhanVienUpdate update);
}
