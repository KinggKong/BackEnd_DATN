package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.StatusBillRequest;
import com.example.be_datn.dto.Response.LichSuHoaDonResponse;
import com.example.be_datn.entity.*;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.mapper.LichSuHoaDonMapper;
import com.example.be_datn.repository.HoaDonRepository;
import com.example.be_datn.repository.LichSuHoaDonRepository;
import com.example.be_datn.repository.LichSuThanhToanRepository;
import com.example.be_datn.repository.NhanVienRepository;
import com.example.be_datn.service.ILichSuHoaDonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LichSuHoaDonService implements ILichSuHoaDonService {
    LichSuHoaDonRepository lichSuHoaDonRepository;
    LichSuHoaDonMapper lichSuHoaDonMapper;
    NhanVienRepository nhanVienRepository;
    HoaDonRepository hoaDonRepository;
    private final LichSuThanhToanRepository lichSuThanhToanRepository;

    @Override
    public LichSuHoaDonResponse insertLichSuHoaDon(StatusBillRequest statusBillRequest) {
        NhanVien nhanVien = new NhanVien();

        if (statusBillRequest.getIdNhanvien() == null) {
            NhanVien existedNhanVien = nhanVienRepository.findByTen("BOT");
            if (existedNhanVien == null) {
                NhanVien newNhanVien = NhanVien.builder()
                        .ten("BOT")
                        .build();
                nhanVien = nhanVienRepository.saveAndFlush(newNhanVien);
            } else {
                nhanVien = existedNhanVien;
            }
        } else {
            nhanVien = nhanVienRepository.findById(statusBillRequest.getIdNhanvien()).orElseThrow(() -> new AppException(ErrorCode.NHANVIEN_NOT_FOUND));
        }


        HoaDon hoaDon = hoaDonRepository.findById(statusBillRequest.getIdHoaDon()).orElseThrow(() -> new AppException(ErrorCode.HOA_DON_NOT_FOUND));

        LichSuHoaDon lichSuHoaDon = LichSuHoaDon.builder()
                .nhanVien(nhanVien)
                .hoaDon(hoaDon)
                .createdBy(nhanVien.getTen())
                .trangThai(statusBillRequest.getStatus())
                .ghiChu(statusBillRequest.getGhiChu())
                .build();
        LichSuHoaDon lichSuHD = lichSuHoaDonRepository.save(lichSuHoaDon);
        hoaDon.setTrangThai(lichSuHD.getTrangThai());
        hoaDonRepository.save(hoaDon);

        if (statusBillRequest.getStatus().equals(StatusPayment.DONE.toString())) {
            LichSuThanhToan lichSuThanhToan = LichSuThanhToan.builder()
                    .hoaDon(hoaDon)
                    .type(hoaDon.getLoaiHoaDon())
                    .soTien(hoaDon.getTongTien())
                    .paymentMethod(hoaDon.getHinhThucThanhToan())
                    .status("DONE")
                    .build();
            lichSuThanhToanRepository.save(lichSuThanhToan);
        }
        return lichSuHoaDonMapper.toResponse(lichSuHD);
    }

    @Override
    public List<LichSuHoaDonResponse> findLichSuHoaDonByIdHoaDon(Long idHoaDon) {
        List<LichSuHoaDon> lichSuHoaDons = lichSuHoaDonRepository.findByHoaDon_Id(idHoaDon);
        return lichSuHoaDonMapper.toListResponse(lichSuHoaDons);
    }
}
