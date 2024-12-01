package com.example.be_datn.dto.Response;

import com.example.be_datn.entity.HinhAnh;
import com.example.be_datn.entity.SanPhamChiTiet;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SanPhamChiTietResponse {
    Long id;
    Long id_mauSac;
    Long id_kichThuoc;
    Long id_sanPham;
    String maSanPham;
    String tenSanPham;
    String tenMauSac;
    String tenKichThuoc;
    int soLuong;
    Double giaBan;
    Double giaBanSauKhiGiam;
    int trangThai;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDateTime updatedAt;
    List<HinhAnh> hinhAnhList;




    public static SanPhamChiTietResponse fromSanPhamChiTiet(SanPhamChiTiet sanPhamChiTiet) {
        return SanPhamChiTietResponse.builder()
                .id(sanPhamChiTiet.getId())
                .id_mauSac(sanPhamChiTiet.getMauSac().getId())
                .id_kichThuoc(sanPhamChiTiet.getKichThuoc().getId())
                .id_sanPham(sanPhamChiTiet.getSanPham().getId())
                .maSanPham(sanPhamChiTiet.getMaSanPham())
                .tenSanPham(sanPhamChiTiet.getSanPham().getTenSanPham())
                .tenMauSac(sanPhamChiTiet.getMauSac().getTenMau())
                .tenKichThuoc(sanPhamChiTiet.getKichThuoc().getTenKichThuoc())
                .soLuong(sanPhamChiTiet.getSoLuong())
                .giaBan(sanPhamChiTiet.getGiaBan())
                .giaBanSauKhiGiam(sanPhamChiTiet.getGiaBanSauKhiGiam())
                .trangThai(sanPhamChiTiet.getTrangThai())
                .createdAt(sanPhamChiTiet.getCreated_at())
                .updatedAt(sanPhamChiTiet.getUpdated_at())
                .hinhAnhList(sanPhamChiTiet.getHinhAnhList() != null ?sanPhamChiTiet.getHinhAnhList() : new ArrayList<>())
                .build();
    }
}
