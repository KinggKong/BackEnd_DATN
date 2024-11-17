package com.example.be_datn.dto.Response;

import com.example.be_datn.entity.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SanPhamResponse {
    Long id;

    String tenSanPham;

    String moTa;

    int trangThai;

    DanhMuc danhMuc;

    ThuongHieu thuongHieu;

    ChatLieuVai chatLieuVai;

    ChatLieuDe chatLieuDe;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDateTime updatedAt;
    List<SanPhamChiTietResponse> sanPhamChiTietList;

    public static SanPhamResponse fromSanPham(SanPham sanPham) {
        return SanPhamResponse.builder()
                .id(sanPham.getId())
                .tenSanPham(sanPham.getTenSanPham())
                .moTa(sanPham.getMoTa())
                .trangThai(sanPham.getTrangThai())
                .danhMuc(sanPham.getDanhMuc())
                .thuongHieu(sanPham.getThuongHieu())
                .chatLieuVai(sanPham.getChatLieuVai())
                .chatLieuDe(sanPham.getChatLieuDe())
                .createdAt(sanPham.getCreated_at())
                .updatedAt(sanPham.getUpdated_at())
                .sanPhamChiTietList(sanPham.getSanPhamChiTietList() != null ?
                        sanPham.getSanPhamChiTietList().stream().map(SanPhamChiTietResponse::fromSanPhamChiTiet).toList() : Collections.emptyList())
                .build();
    }
}
