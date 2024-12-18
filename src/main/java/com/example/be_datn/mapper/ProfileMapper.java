package com.example.be_datn.mapper;

import com.example.be_datn.dto.Response.Profile;
import com.example.be_datn.entity.KhachHang;
import com.example.be_datn.entity.NhanVien;
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

    public Profile toProfileAdmin(NhanVien nhanVien) {
        return Profile.builder()
                .id(nhanVien.getId())
                .ten(nhanVien.getTen())
                .email(nhanVien.getEmail())
                .sdt(nhanVien.getSdt())
                .avatar(nhanVien.getAvatar())
                .ngaySinh(nhanVien.getNgaySinh())
                .gioiTinh(nhanVien.getGioiTinh())
                .build();
    }
}
