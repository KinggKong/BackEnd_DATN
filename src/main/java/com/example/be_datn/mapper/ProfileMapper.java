package com.example.be_datn.mapper;

import com.example.be_datn.dto.Response.Profile;
import com.example.be_datn.entity.KhachHang;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {
    public Profile toProfile(KhachHang khachHang, Long idGiohang) {
        return Profile.builder()
                .id(khachHang.getId())
                .ten(khachHang.getTen())
                .ma(khachHang.getMa())
                .email(khachHang.getEmail())
                .sdt(khachHang.getSdt())
                .avatar(khachHang.getAvatar())
                .ngaySinh(khachHang.getNgaySinh())
                .gioiTinh(khachHang.getGioiTinh())
                .idGioHang(idGiohang)
                .build();
    }
}
