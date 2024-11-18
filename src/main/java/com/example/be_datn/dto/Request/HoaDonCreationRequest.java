package com.example.be_datn.dto.Request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HoaDonCreationRequest {
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
    String hinhThucThanhToan;
    Integer trangThai;
    Long idVoucher;
    Double soTienGiam;
}
