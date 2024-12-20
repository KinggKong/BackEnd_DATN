package com.example.be_datn.dto.Response;

import com.example.be_datn.entity.GioHang;
import com.example.be_datn.entity.SanPham;
import com.example.be_datn.entity.SanPhamChiTiet;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GioHangChiTietResponse {
    private Long id;
    GioHang gioHang;
    SanPhamChiTietResponse sanPhamChiTietResponse;
    int soLuong;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Double giaTien;
    Integer trangThai;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime thoiGianGiamGia;
}
