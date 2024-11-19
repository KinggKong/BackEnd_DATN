package com.example.be_datn.dto.Response;

import java.time.LocalDateTime;

public class GioHang_CtResponse {
    private Long idGioHang;
    SanPhamChiTietResponse sanPhamChiTietResponse;
    private int soLuong;
    private Double giaTien;
    private Integer trangThai;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
