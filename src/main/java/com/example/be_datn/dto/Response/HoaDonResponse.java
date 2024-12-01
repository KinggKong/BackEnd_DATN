package com.example.be_datn.dto.Response;

import com.example.be_datn.entity.HoaDon;
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
    String tenNhanVien;
    String tenKhachHang;
    String hinhThucThanhToan;
    String trangThai;
    String maVoucher;
    Double soTienGiam;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;


    public static HoaDonResponse from(HoaDon hoaDon) {
        if (hoaDon == null) {
            return null;
        }
       return HoaDonResponse.builder()
               .maHoaDon(hoaDon.getMaHoaDon())
               .tenNguoiNhan(hoaDon.getTenNguoiNhan())
               .diaChiNhan(hoaDon.getDiaChiNhan())
               .sdt(hoaDon.getSdt())
               .tongTien(hoaDon.getTongTien())
               .tienSauGiam(hoaDon.getTienSauGiam())
               .tienShip(hoaDon.getTienShip())
               .ghiChu(hoaDon.getGhiChu())
               .loaiHoaDon(hoaDon.getLoaiHoaDon())
               .email(hoaDon.getEmail())
               .tenNhanVien(hoaDon.getNhanVien().getTen())
               .tenKhachHang(hoaDon.getKhachHang().getTen())
               .hinhThucThanhToan(hoaDon.getHinhThucThanhToan())
               .maVoucher(hoaDon.getVoucher().getMaVoucher())
               .soTienGiam(hoaDon.getSoTienGiam())
               .trangThai(hoaDon.getTrangThai())
               .build();
    }

}
