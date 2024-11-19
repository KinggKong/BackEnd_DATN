package com.example.be_datn.dto.Request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GioHangChiTietRequest {
    Long idGioHang;
    Long idSanPhamChiTiet;
    int soLuong;
    Double giaTien;
    int id_khachHang;
}
