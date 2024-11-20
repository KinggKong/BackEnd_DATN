package com.example.be_datn.mapper;

import com.example.be_datn.dto.Response.SanPhamResponse;
import com.example.be_datn.entity.SanPham;

import java.util.List;

public class SanPhamMapper {
    public static SanPhamResponse toSanPhamResponse(SanPham sanPham) {
        return SanPhamResponse.builder()
                .tenSanPham(sanPham.getTenSanPham())
                .moTa(sanPham.getMoTa())
                .id(sanPham.getId())
                .build();
    }

    public static List<SanPhamResponse> toSanPhamResponses(List<SanPham> sanPhams) {
        return sanPhams.stream().map(SanPhamMapper::toSanPhamResponse).toList();
    }
}
