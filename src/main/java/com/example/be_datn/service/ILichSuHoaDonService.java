package com.example.be_datn.service;

import com.example.be_datn.dto.Request.LichSuThanhToanCreationRequest;
import com.example.be_datn.entity.LichSuThanhToan;

public interface ILichSuHoaDonService {
    LichSuThanhToan create(LichSuThanhToanCreationRequest request);
    void delete(Long id);
}
