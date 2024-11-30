package com.example.be_datn.mapper;

import com.example.be_datn.dto.Response.HoaDonResponse;
import com.example.be_datn.entity.HoaDon;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HoaDonMapper {
    public HoaDonResponse toHoaDonResponse(HoaDon hoaDon) {
        return HoaDonResponse.builder()
                .id(hoaDon.getId())
                .maHoaDon(hoaDon.getMaHoaDon())
                .tenNguoiNhan(hoaDon.getTenNguoiNhan())
                .sdt(hoaDon.getSdt())
                .tongTien(hoaDon.getTongTien())
                .tienSauGiam(hoaDon.getTienSauGiam())
                .tienShip(hoaDon.getTienShip())
                .ghiChu(hoaDon.getGhiChu())
                .loaiHoaDon(hoaDon.getLoaiHoaDon())
                .email(hoaDon.getEmail())
                .diaChiNhan(hoaDon.getDiaChiNhan())
                .tenKhachHang(hoaDon.getKhachHang().getTen())
                .hinhThucThanhToan(hoaDon.getHinhThucThanhToan())
                .trangThai(hoaDon.getTrangThai())
                .maVoucher(hoaDon.getVoucher() != null ? hoaDon.getVoucher().getMaVoucher() : null)
//                .maVoucher(Optional.ofNullable(hoaDon.getVoucher()).map(Voucher::getMaVoucher).orElse(null))
                .soTienGiam(hoaDon.getSoTienGiam())
                .tenKhachHang(hoaDon.getKhachHang().getTen())
                .createdAt(hoaDon.getCreated_at())
                .updatedAt(hoaDon.getUpdated_at())
                .build();
    }

    public List<HoaDonResponse> toListResponse(List<HoaDon> hoaDons) {
        return hoaDons.stream().map(this::toHoaDonResponse).toList();
    }
}
