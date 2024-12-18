package com.example.be_datn.mapper;

import com.example.be_datn.dto.Response.HoaDonChiTietResponse;
import com.example.be_datn.dto.Response.SanPhamChiTietResponse;
import com.example.be_datn.entity.GioHangChiTiet;
import com.example.be_datn.entity.HoaDon;
import com.example.be_datn.entity.HoaDonCT;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HoaDonChiTietMapper {
    public HoaDonCT toHoaDonCT(GioHangChiTiet gioHangChiTiet, Long idHoaDon) {
        HoaDon hoaDon = HoaDon.builder()
                .id(idHoaDon)
                .build();
        return HoaDonCT.builder()
                .hoaDon(hoaDon)
                .sanPhamChiTiet(gioHangChiTiet.getSanPhamChiTiet())
                .soLuong(gioHangChiTiet.getSoLuong())
                .giaTien(gioHangChiTiet.getSanPhamChiTiet().getGiaBanSauKhiGiam())
                .trangThai(1)
                .build();
    }

    public HoaDonChiTietResponse toResponse(HoaDonCT hoaDonCT) {
        return HoaDonChiTietResponse.builder()
                .id(hoaDonCT.getId())
                .sanPhamChiTietResponse(SanPhamChiTietResponse.fromSanPhamChiTiet(hoaDonCT.getSanPhamChiTiet()))
                .soLuong(hoaDonCT.getSoLuong())
                .giaTien(hoaDonCT.getGiaTien())
                .build();
    }

    public List<HoaDonChiTietResponse> toListResponse(List<HoaDonCT> hoaDonCTS) {
        return hoaDonCTS.stream().map(this::toResponse).toList();
    }
}
