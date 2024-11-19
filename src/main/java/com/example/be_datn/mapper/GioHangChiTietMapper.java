package com.example.be_datn.mapper;

import com.example.be_datn.dto.Response.GioHangChiTietResponse;
import com.example.be_datn.dto.Response.SanPhamChiTietResponse;
import com.example.be_datn.entity.GioHangChiTiet;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class GioHangChiTietMapper {

    public GioHangChiTietResponse toGioHangChiTietResponse(GioHangChiTiet gioHangChiTiet) {
        return GioHangChiTietResponse.builder()
                .id(gioHangChiTiet.getId())
                .gioHang(gioHangChiTiet.getGioHang())
                .sanPhamChiTietResponse(SanPhamChiTietResponse.fromSanPhamChiTiet(gioHangChiTiet.getSanPhamChiTiet()))
                .soLuong(gioHangChiTiet.getSoLuong())
                .trangThai(gioHangChiTiet.getTrangThai())
                .updatedAt(gioHangChiTiet.getUpdated_at())
                .createdAt(gioHangChiTiet.getCreated_at())
                .giaTien(gioHangChiTiet.getGiaTien())
                .build();
    }

    public List<GioHangChiTietResponse> toListGioHangCtResponse(List<GioHangChiTiet> gioHangCtList) {
        if (gioHangCtList.isEmpty()) {
            return Collections.emptyList();
        }
        return gioHangCtList.stream().map(this::toGioHangChiTietResponse).toList();
    }


}
