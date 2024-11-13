package com.example.be_datn.mapper;

import com.example.be_datn.dto.Request.TaiKhoanRequest;
import com.example.be_datn.dto.Response.TaiKhoanResponse;
import com.example.be_datn.entity.TaiKhoan;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaiKhoanMapper {
    TaiKhoan toTaiKhoan(TaiKhoanRequest taiKhoanRequest);

    void updateTaiKhoan(@MappingTarget TaiKhoan taiKhoan, TaiKhoanRequest taiKhoanRequest);

    TaiKhoanResponse toTaiKhoanResponse(TaiKhoan taiKhoan);

    List<TaiKhoanResponse> toListTaiKhoanResponse(List<TaiKhoan> taiKhoanList);

}
