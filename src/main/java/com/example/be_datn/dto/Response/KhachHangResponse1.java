package com.example.be_datn.dto.Response;

import com.example.be_datn.entity.KhachHang1;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhachHangResponse1 {

    private Long id;
    private String ten;
    private String email;
    private String sdt;
    private String avatar;
    private String ma;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate ngaySinh;

    private Long idDiaChi;
    private Long idTaiKhoan;
    private Boolean gioiTinh;
    private Integer trangThai;

    // Phương thức chuyển đổi từ KhachHang1 sang KhachHangResponse
    public static KhachHangResponse1 fromKhachHang(KhachHang1 khachHang) {
        return KhachHangResponse1.builder()
                .id(khachHang.getId())
                .ten(khachHang.getTen())
                .ma(khachHang.getMa())
                .email(khachHang.getEmail())
                .sdt(khachHang.getSdt())
                .avatar(khachHang.getAvatar())
                .ngaySinh(khachHang.getNgaySinh())
                .idDiaChi(khachHang.getIdDiaChi())
                .idTaiKhoan(khachHang.getIdTaiKhoan())
                .gioiTinh(khachHang.getGioiTinh())
                .trangThai(khachHang.getTrangThai())
                .build();
    }
}
