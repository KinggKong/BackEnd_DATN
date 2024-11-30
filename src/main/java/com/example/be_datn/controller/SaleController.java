package com.example.be_datn.controller;

import com.example.be_datn.dto.Request.SaleRequest;
import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Response.SaleResponse;
import com.example.be_datn.service.impl.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sale")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SaleController {
    SaleService saleService;
    @GetMapping("")
    public ApiResponse<Page<SaleResponse>> getAll(
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(value = "tenChienDich", defaultValue = "") String tenChienDich,
            @RequestParam(value = "ngayBatDau", required = false) String ngayBatDauStr,
            @RequestParam(value = "ngayKetThuc", required = false) String ngayKetThucStr,
            @RequestParam(value = "trangThai",defaultValue = "") Integer trangThai) {

        ApiResponse<Page<SaleResponse>> apiResponse = new ApiResponse<>();
        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize));

        LocalDateTime ngayBatDau = null;
        LocalDateTime ngayKetThuc = null;


        if (ngayBatDauStr != null && !ngayBatDauStr.isEmpty()) {
            ngayBatDau = LocalDateTime.parse(ngayBatDauStr); // Chuyển đổi String thành LocalDateTime
        }
        if (ngayKetThucStr != null && !ngayKetThucStr.isEmpty()) {
            ngayKetThuc = LocalDateTime.parse(ngayKetThucStr);
        }


        apiResponse.setData(saleService.getAll(tenChienDich, ngayBatDau, ngayKetThuc, trangThai, pageable));
        return apiResponse;
    }
    @PostMapping("")
    public ApiResponse<SaleResponse> create(@RequestBody SaleRequest saleRequest) {
        ApiResponse<SaleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(saleService.create(saleRequest));
        return apiResponse;
    }
    @GetMapping("/getAllTenChienDich")
    public List<String> getAllTenChienDich() {
        return saleService.getAllTenChienDich();
    }
    @GetMapping("/{id}")
    public ApiResponse<SaleResponse> getById(@PathVariable Long id) {
        ApiResponse<SaleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(saleService.getById(id));
        return apiResponse;
    }
    @PutMapping("/{id}")
    public ApiResponse<SaleResponse> update(@RequestBody SaleRequest saleRequest, @PathVariable Long id) {
        ApiResponse<SaleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(saleService.update(saleRequest, id));
        return apiResponse;
    }
    @PutMapping("/status/{id}")
    public ApiResponse<SaleResponse> updateStatus(@PathVariable Long id) {
        ApiResponse<SaleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(saleService.updateStatus(id, 0));
        return apiResponse;
    }



}
