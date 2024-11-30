package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.LichSuThanhToanCreationRequest;
import com.example.be_datn.entity.HoaDon;
import com.example.be_datn.entity.LichSuThanhToan;
import com.example.be_datn.entity.Payment;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.HoaDonRepository;
import com.example.be_datn.repository.LichSuThanhToanRepository;
import com.example.be_datn.service.ILichSuThanhToanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LichSuThanhToanService implements ILichSuThanhToanService {

    private final LichSuThanhToanRepository lichSuThanhToanRepository;

    private final HoaDonRepository hoaDonRepository;

    public String generateMaGiaoDich(){
        StringBuilder transactionCode = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());
        transactionCode.append(timestamp);
        String randomPart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        transactionCode.append(randomPart);
        return transactionCode.toString();
    }

    @Override
    public LichSuThanhToan create(LichSuThanhToanCreationRequest request) {
        HoaDon hoaDon = hoaDonRepository.findById(request.getHoaDonId())
                .orElseThrow(() -> new AppException(ErrorCode.HOA_DON_NOT_FOUND));
        String maGiaoDich = request.getMaGiaoDich();
        if(request.getPhuongThucThanhToan().equalsIgnoreCase(Payment.CASK.toString())){
            maGiaoDich = "";
        }else {
            maGiaoDich = generateMaGiaoDich();
        }
        LichSuThanhToan response = LichSuThanhToan.builder()
                .hoaDon(hoaDon)
                .maGiaoDich(maGiaoDich)
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
