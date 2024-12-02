package com.example.be_datn.dto.Request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HoaDonRequest {
    Long idGioHang;

    String tenNguoiNhan;

    String diaChiNhan;

    String sdt;

    Double tongTien;

    Double tienSauGiam;

    Double tienShip;

    String ghiChu;

    String email;

    Long idKhachHang;

    Long idVoucher;

    String hinhThucThanhToan;

    Double soTienGiam;

}
