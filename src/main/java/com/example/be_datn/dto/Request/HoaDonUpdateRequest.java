package com.example.be_datn.dto.Request;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HoaDonUpdateRequest {
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
    Long idNhanVien;
    Long idKhachHang;
    Integer soLuong;
    String hinhThucThanhToan;
    Integer trangThai;
    Long idVoucher;
    Double soTienGiam;
}
