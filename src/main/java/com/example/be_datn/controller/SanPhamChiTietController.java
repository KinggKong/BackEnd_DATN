package com.example.be_datn.controller;

import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Response.SanPhamChiTietResponse;
import com.example.be_datn.service.impl.SanPhamChiTietService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sanphamchitiets")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SanPhamChiTietController {
    SanPhamChiTietService sanPhamChiTietService;
    @GetMapping("")
    public ApiResponse<Page<SanPhamChiTietResponse>> getAllSanPhamChiTiet(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                                          @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize));
        ApiResponse<Page<SanPhamChiTietResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(sanPhamChiTietService.getAllPage(pageable));
        return apiResponse;
    }
}
