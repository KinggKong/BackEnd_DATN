package com.example.be_datn.mapper;

import com.example.be_datn.dto.Response.LichSuHoaDonResponse;
import com.example.be_datn.entity.LichSuHoaDon;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LichSuHoaDonMapper {
    public LichSuHoaDonResponse toResponse(LichSuHoaDon lichSuHoaDondon) {
        return LichSuHoaDonResponse.builder()
                .createdAt(lichSuHoaDondon.getCreated_at())
                .ghiChu(lichSuHoaDondon.getGhiChu())
                .trangThai(lichSuHoaDondon.getTrangThai())
                .build();
    }

    public List<LichSuHoaDonResponse> toListResponse(List<LichSuHoaDon> lichSuHoaDonList) {
        return lichSuHoaDonList.stream().map(this::toResponse).toList();
    }

}
