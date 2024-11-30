package com.example.be_datn.dto.Response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonCTResponse {
    Long id;
    Long idGioHang;
    Long idSanPhamChiTiet;
    String tenSanPhamChiTiet;
    Integer soLuong;
    Double giaBan;
    Double tongTien;

}
