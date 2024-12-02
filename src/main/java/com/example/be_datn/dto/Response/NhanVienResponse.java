package com.example.be_datn.dto.Response;

import com.example.be_datn.entity.NhanVien;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhanVienResponse {

    private Long id;
    private String ten;
    private String email;
    private String sdt;
    private String avatar;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate ngaySinh;

    private String diaChi;
    private Boolean gioiTinh;
    private Integer trangThai;

    // Phương thức chuyển đổi từ NhanVien sang NhanVienResponse
    public static NhanVienResponse fromNhanVien(NhanVien nhanVien) {
        return NhanVienResponse.builder()
                .id(nhanVien.getId())
                .ten(nhanVien.getTen())
                .email(nhanVien.getEmail())
                .sdt(nhanVien.getSdt())
                .avatar(nhanVien.getAvatar())
                .ngaySinh(nhanVien.getNgaySinh())
                .diaChi(nhanVien.getDiaChi())
                .gioiTinh(nhanVien.getGioiTinh())
                .trangThai(nhanVien.getTrangThai())
                .build();
    }
}