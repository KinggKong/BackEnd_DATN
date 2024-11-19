package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Response.ThongKeResponse;

import com.example.be_datn.repository.HoaDonRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ThongKeService {
    HoaDonRepository thongKeRepository;
    public ThongKeResponse getAll(LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc) {
        ThongKeResponse thongKeResponse = new ThongKeResponse();
        thongKeResponse.setDonThanhCong(thongKeRepository.countByTrangThai("Done", ngayBatDau, ngayKetThuc));
        thongKeResponse.setDonHuy(thongKeRepository.countByTrangThai("CANCELLED", ngayBatDau, ngayKetThuc));
        thongKeResponse.setChoXacNhan(thongKeRepository.countByTrangThai("WAITING", ngayBatDau, ngayKetThuc));
        thongKeResponse.setDangGiaoHang(thongKeRepository.countByTrangThai("SHIPPING", ngayBatDau, ngayKetThuc));
        thongKeResponse.setChoLayHang(thongKeRepository.countByTrangThai("ACCEPTED", ngayBatDau, ngayKetThuc));

        Float tongDoanhThu = thongKeRepository.tongDoanhThu(ngayBatDau, ngayKetThuc);
        Integer tongSanPhamBan = thongKeRepository.tongSanPhamBan(ngayBatDau, ngayKetThuc);
        if(tongDoanhThu!=null){
            thongKeResponse.setTongDoanhThu(tongDoanhThu);
        }else {
            thongKeResponse.setTongDoanhThu(0.0);
        }
        if(tongSanPhamBan!=null){
            thongKeResponse.setTongSanPhamBan(tongSanPhamBan);
        }else {
            thongKeResponse.setTongSanPhamBan(0);
        }

        return thongKeResponse;

    }

}

