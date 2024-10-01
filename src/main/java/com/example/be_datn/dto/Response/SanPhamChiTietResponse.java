package com.example.be_datn.dto.Response;

import com.example.be_datn.entity.SanPhamChiTiet;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

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
    int soLuong;
    Double giaBan;
    int trangThai;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDateTime updatedAt;
    public static SanPhamChiTietResponse fromSanPhamChiTiet(SanPhamChiTiet sanPhamChiTiet) {
        return SanPhamChiTietResponse.builder()
                .id(sanPhamChiTiet.getId())
                .id_mauSac(sanPhamChiTiet.getMauSac().getId())
                .id_kichThuoc(sanPhamChiTiet.getKichThuoc().getId())
                .id_sanPham(sanPhamChiTiet.getSanPham().getId())
                .maSanPham(sanPhamChiTiet.getMaSanPham())
                .soLuong(sanPhamChiTiet.getSoLuong())
                .giaBan(sanPhamChiTiet.getGiaBan())
                .trangThai(sanPhamChiTiet.getTrangThai())
                .createdAt(sanPhamChiTiet.getCreated_at())
                .updatedAt(sanPhamChiTiet.getUpdated_at())
                .build();
    }
}
