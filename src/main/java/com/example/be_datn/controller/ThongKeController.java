package com.example.be_datn.controller;

import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Response.ThongKeResponse;

import com.example.be_datn.dto.Response.ThongKeSanPhamBanChayResponse;
import com.example.be_datn.dto.Response.ViecCanLamResponse;
import com.example.be_datn.service.impl.ThongKeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/thongke")
public class ThongKeController {
    ThongKeService thongKeSerVice;
    @GetMapping("")
    public ApiResponse<ThongKeResponse> getThongKe(@RequestParam(value = "ngayBatDau", defaultValue = "") String ngayBatDau,
                                                   @RequestParam(value = "ngayKetThuc", defaultValue = "") String ngayKetThuc,
                                                   @RequestParam(value = "typeSale", defaultValue = "") String typeSale) {
        ApiResponse<ThongKeResponse> apiResponse = new ApiResponse<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault());
        LocalDateTime date1 = LocalDate.parse(ngayBatDau, formatter).atStartOfDay();
        LocalDateTime date2 = LocalDate.parse(ngayKetThuc, formatter).atTime(23, 59, 59);
//        Instant date1 = LocalDate.parse(ngayBatDau, formatter).atStartOfDay(ZoneId.systemDefault()).toInstant();
//        Instant date2 = LocalDate.parse(ngayKetThuc,formatter).atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
        //Instant date2 = LocalDate.parse(ngayKetThuc, formatter).atStartOfDay(ZoneId.systemDefault()).toInstant();
        apiResponse.setData(thongKeSerVice.getAll(date1,date2,typeSale));
        return apiResponse;
    }

    @GetMapping("doanh-thu")
    public ApiResponse<List<Map<String,Object>>> getThongKeDoanhThu(@RequestParam(value = "ngayBatDau", defaultValue = "") String ngayBatDau,
                                                     @RequestParam(value = "ngayKetThuc", defaultValue = "") String ngayKetThuc) {
        ApiResponse<List<Map<String,Object>>> apiResponse = new ApiResponse<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault());
        LocalDateTime date1 = LocalDate.parse(ngayBatDau, formatter).atStartOfDay();
        LocalDateTime date2 = LocalDate.parse(ngayKetThuc, formatter).atTime(23, 59, 59);
//        Instant date1 = LocalDate.parse(ngayBatDau, formatter).atStartOfDay(ZoneId.systemDefault()).toInstant();
//        Instant date2 = LocalDate.parse(ngayKetThuc,formatter).atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
        //Instant date2 = LocalDate.parse(ngayKetThuc, formatter).atStartOfDay(ZoneId.systemDefault()).toInstant();
        apiResponse.setData(thongKeSerVice.layDoanhThuTheoThoiGian(date1,date2));
        return apiResponse;
    }
    @GetMapping("doanh-thu-ngay")
    public ApiResponse<List<Map<String,Object>>> getThongKeDoanhThuNgay(@RequestParam(value = "ngayBatDau", defaultValue = "") String ngayBatDau,
                                                                        @RequestParam(value = "ngayKetThuc", defaultValue = "") String ngayKetThuc,
                                                                        @RequestParam(value = "filter", defaultValue = "") String filter,
                                                                        @RequestParam(value = "typeSale", defaultValue = "") String typeSale) {
        ApiResponse<List<Map<String,Object>>> apiResponse = new ApiResponse<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault());
        LocalDateTime date1 = LocalDate.parse(ngayBatDau, formatter).atStartOfDay();
        LocalDateTime date2 = LocalDate.parse(ngayKetThuc, formatter).atTime(23, 59, 59);
        apiResponse.setData(thongKeSerVice.layDoanhThuTheoThoiGianNgay(filter,date1,date2,typeSale));
        return apiResponse;
    }
    @GetMapping("doanh-thu-san-pham")
    public ApiResponse<List<Map<String,Object>>> getThongKeDoanhThuSanPham(@RequestParam(value = "ngayBatDau", defaultValue = "") String ngayBatDau,
                                                                        @RequestParam(value = "ngayKetThuc", defaultValue = "") String ngayKetThuc,
                                                                           @RequestParam(value = "filter", defaultValue = "") String filter,
                                                                           @RequestParam(value = "typeSale", defaultValue = "") String typeSale) {
        ApiResponse<List<Map<String,Object>>> apiResponse = new ApiResponse<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault());
        LocalDateTime date1 = LocalDate.parse(ngayBatDau, formatter).atStartOfDay();
        LocalDateTime date2 = LocalDate.parse(ngayKetThuc, formatter).atTime(23, 59, 59);
        apiResponse.setData(thongKeSerVice.getDoanhThuTheoSanPham(filter,date1,date2,typeSale));
        return apiResponse;
    }
    @GetMapping("san-pham-ban-chay")
    public ApiResponse<List<ThongKeSanPhamBanChayResponse>> getThongKeSanPhamBanChay(@RequestParam(value = "ngayBatDau", defaultValue = "") String ngayBatDau,
                                                                                     @RequestParam(value = "ngayKetThuc", defaultValue = "") String ngayKetThuc,
                                                                                     @RequestParam(value = "typeSale", defaultValue = "") String typeSale) {
        ApiResponse<List<ThongKeSanPhamBanChayResponse>> apiResponse = new ApiResponse<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault());
        LocalDateTime date1 = LocalDate.parse(ngayBatDau, formatter).atStartOfDay();
        LocalDateTime date2 = LocalDate.parse(ngayKetThuc, formatter).atTime(23, 59, 59);
        apiResponse.setData(thongKeSerVice.getSanPhambanChayDoanhThu(date1,date2,typeSale));
        return apiResponse;
    }
    @GetMapping("viec-can-lam")
    public ApiResponse<ViecCanLamResponse> getViecCanLam() {
        ApiResponse<ViecCanLamResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(thongKeSerVice.getViecCanLam());
        return apiResponse;
    }

}


