package com.example.be_datn.dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class HoaDonResponse {
    Long id;

    String maHoaDon;

    String tenNguoiNhan;

    String diaChiNhan;

    String sdt;

    Double tongTien;

    Double tienSauGiam;

    Double tienShip;

    String ghiChu;

    String loaiHoaDon;

    String email;

    String tenKhachHang;

    String hinhThucThanhToan;

    String trangThai;

    String maVoucher;

    Float soTienGiam;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
