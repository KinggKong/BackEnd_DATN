package com.example.be_datn.dto.Response;


import com.example.be_datn.entity.HinhAnh;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    List<HinhAnh> hinhAnhList;
}
