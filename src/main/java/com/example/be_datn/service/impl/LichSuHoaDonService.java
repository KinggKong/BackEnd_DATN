package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.LichSuThanhToanCreationRequest;
import com.example.be_datn.entity.HoaDon;
import com.example.be_datn.entity.LichSuThanhToan;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.HoaDonChiTietRepository;
import com.example.be_datn.repository.HoaDonRepository;
import com.example.be_datn.repository.LichSuThanhToanRepository;
import com.example.be_datn.service.ILichSuHoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LichSuHoaDonService implements ILichSuHoaDonService {

    private final LichSuThanhToanRepository lichSuThanhToanRepository;

    private final HoaDonRepository hoaDonRepository;

    @Override
    public LichSuThanhToan create(LichSuThanhToanCreationRequest request) {
        HoaDon hoaDon = hoaDonRepository.findById(request.getHoaDonId())
                .orElseThrow(() -> new AppException(ErrorCode.HOA_DON_NOT_FOUND));
        LichSuThanhToan response = LichSuThanhToan.builder()
                .hoaDon(hoaDon)
                .maGiaoDich(request.getMaGiaoDich())
                .soTien(hoaDon.getTongTien())
                .phuongThucThanhToan(request.getPhuongThucThanhToan())
                .build();
        return lichSuThanhToanRepository.save(response);
    }

    @Override
    public void delete(Long id) {
        LichSuThanhToan lichSuThanhToan = lichSuThanhToanRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LICH_SU_THANH_TOAN_NOT_FOUND));
        lichSuThanhToanRepository.delete(lichSuThanhToan);
    }
}
