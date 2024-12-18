package com.example.be_datn.mapper;


import com.example.be_datn.dto.Response.DiaChiResponse;
import com.example.be_datn.dto.Response.KhachHangResponse;
import com.example.be_datn.entity.DiaChi;
import com.example.be_datn.entity.KhachHang;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KhachHangMapper {
    public  KhachHangResponse toKhachHangResponse(KhachHang khachHang) {

//        DiaChi diaChi = (khachHang.getDiaChiList() != null && !khachHang.getDiaChiList().isEmpty())
//                ? khachHang.getDiaChiList().get(0)
//                : null;
        return KhachHangResponse.builder()
                .id(khachHang.getId())
                .ten(khachHang.getTen())
                .email(khachHang.getEmail())
                .sdt(khachHang.getSdt())
                .avatar(khachHang.getAvatar())
                .ma(khachHang.getMa())
                .ngaySinh(khachHang.getNgaySinh())
                .gioiTinh(khachHang.getGioiTinh())
                .trangThai(khachHang.getTrangThai())
                .diaChiStr(khachHang.getDiaChi())
//                .diaChi(DiaChiResponse.toResponse(diaChi))
                .build();
    }

    public  List<KhachHangResponse> toListKhachHangResponse(List<KhachHang> list) {
        return list.stream().map(this::toKhachHangResponse).toList();
    }
}
