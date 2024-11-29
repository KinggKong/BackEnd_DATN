package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Response.ThongKeResponse;

import com.example.be_datn.dto.Response.ThongKeSanPhamBanChayResponse;
import com.example.be_datn.repository.HoaDonRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ThongKeService {
    HoaDonRepository thongKeRepository;
    private final HoaDonRepository hoaDonRepository;

    public ThongKeResponse getAll(LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, String typeSale) {
        ThongKeResponse thongKeResponse = new ThongKeResponse();
        thongKeResponse.setDonThanhCong(thongKeRepository.countByTrangThai("DONE", ngayBatDau, ngayKetThuc, typeSale));
        thongKeResponse.setDonHuy(thongKeRepository.countByTrangThai("CANCELLED", ngayBatDau, ngayKetThuc, typeSale));
        thongKeResponse.setChoXacNhan(thongKeRepository.countByTrangThai("WAITING", ngayBatDau, ngayKetThuc, typeSale));
        thongKeResponse.setDangGiaoHang(thongKeRepository.countByTrangThai("SHIPPING", ngayBatDau, ngayKetThuc, typeSale));
        thongKeResponse.setChoLayHang(thongKeRepository.countByTrangThai("ACCEPTED", ngayBatDau, ngayKetThuc, typeSale));

        Float tongDoanhThu = thongKeRepository.tongDoanhThuNative(ngayBatDau, ngayKetThuc,typeSale);
        Integer tongSanPhamBan = thongKeRepository.tongSanPhamBanNative(ngayBatDau, ngayKetThuc,typeSale);
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

    public List<Map<String, Object>> layDoanhThuTheoThoiGian(LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc) {
        return hoaDonRepository.doanhThuTheoThoiGian(ngayBatDau, ngayKetThuc);
    }
//    public List<Map<String, Object>> layDoanhThuTheoThoiGianNgay(LocalDateTime ngayBatDau) {
//        return hoaDonRepository.getRevenueByDayInMonth(ngayBatDau);
//    }
    public List<Map<String, Object>> layDoanhThuTheoThoiGianNgay(String filter,LocalDateTime ngayBatDau ,LocalDateTime ngayKetThuc,String typeSale) {
        switch (filter)
        {
            case "date":
                return hoaDonRepository.getRevenueByDayInPeriod(ngayBatDau, ngayKetThuc,typeSale);
            case "week":
                return hoaDonRepository.getRevenueByDayInPeriod(ngayBatDau, ngayKetThuc,typeSale);
            case "month":
                return hoaDonRepository.getRevenueByDayInPeriod(ngayBatDau, ngayKetThuc,typeSale);
            case "year":
                // Lấy năm từ ngày nhập vào
                int year = ngayBatDau.getYear();

                // Chuyển thành ngày đầu tiên của năm
                LocalDateTime startOfYear = LocalDateTime.of(year, 1, 1, 0, 0);
                return hoaDonRepository.getDoanhThuTheoThang(startOfYear,typeSale);
            default:
                return hoaDonRepository.getRevenueByDayInPeriod(ngayBatDau, ngayKetThuc,typeSale);
        }

    }

    public List<Map<String, Object>> getDoanhThuTheoSanPham(String filter,LocalDateTime startDate, LocalDateTime endDate,String typeSale) {
        switch (filter)
        {
            case "date":
                return hoaDonRepository.getDoanhThuTheoSanPham(startDate, endDate,typeSale);
            case "week":
                return hoaDonRepository.getDoanhThuTheoSanPham(startDate, endDate,typeSale);
            case "month":
                return hoaDonRepository.getDoanhThuTheoSanPham(startDate, endDate,typeSale);
            case "year":
                // Lấy năm từ ngày nhập vào
                int year = startDate.getYear();

                // Chuyển thành ngày đầu tiên của năm
                LocalDateTime startOfYear = LocalDateTime.of(year, 1, 1, 0, 0);
                return hoaDonRepository.getDoanhThuTheoSanPhamVaThang(startOfYear,typeSale);

            default:
                return hoaDonRepository.getDoanhThuTheoSanPham(startDate, endDate,typeSale);
        }



    }
    public List<ThongKeSanPhamBanChayResponse> getSanPhambanChayDoanhThu(LocalDateTime startDate, LocalDateTime endDate,String typeSale) {

        List<Object[]> list = hoaDonRepository.findBestSellingProductsNative(startDate, endDate,typeSale);
        List<ThongKeSanPhamBanChayResponse> thongKeSanPhamBanChayResponses = new ArrayList<>();
        for (Object[] objects : list) {
            ThongKeSanPhamBanChayResponse thongKeSanPhamBanChayResponse = new ThongKeSanPhamBanChayResponse();
            thongKeSanPhamBanChayResponse.setTenSanPham(objects[0].toString());
            thongKeSanPhamBanChayResponse.setTongSoLuongBan(Integer.parseInt(objects[1].toString()));
            thongKeSanPhamBanChayResponse.setTongDoanhThu(Double.parseDouble(objects[2].toString()));
            thongKeSanPhamBanChayResponses.add(thongKeSanPhamBanChayResponse);
        }
        return thongKeSanPhamBanChayResponses;
    }
}

