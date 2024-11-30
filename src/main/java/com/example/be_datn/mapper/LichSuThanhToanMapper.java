package com.example.be_datn.mapper;

import com.example.be_datn.dto.Response.LichSuThanhToanResponse;
import com.example.be_datn.entity.LichSuThanhToan;
import org.springframework.stereotype.Component;

@Component
public class LichSuThanhToanMapper {
    public LichSuThanhToanResponse toResponse(LichSuThanhToan lichSuThanhToan) {
        return LichSuThanhToanResponse.builder()
                .id(lichSuThanhToan.getId())
                .maHoaDon(lichSuThanhToan.getHoaDon().getMaHoaDon())
                .paymentMethod(lichSuThanhToan.getPaymentMethod())
                .soTien(lichSuThanhToan.getSoTien())
                .status(lichSuThanhToan.getStatus())
                .type(lichSuThanhToan.getType())
                .createdAt(lichSuThanhToan.getCreated_at())
                .build();
    }
}
