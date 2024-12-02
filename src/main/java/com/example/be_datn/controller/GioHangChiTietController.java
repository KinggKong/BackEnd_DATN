package com.example.be_datn.controller;

import com.example.be_datn.dto.Request.GioHangChiTietRequest;
import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.service.impl.GioHangChiTietService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gio-hang-ct")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GioHangChiTietController {
    GioHangChiTietService gioHangChiTietService;

    @GetMapping("")
    public ApiResponse<?> getAllGioHangChiTiet(@RequestParam Long idGioHang) {
        return ApiResponse.builder()
                .code(1000)
                .data(gioHangChiTietService.findByIdGioHang(idGioHang))
                .message("get all successfull")
                .build();
    }

    @PostMapping("")
    public ApiResponse<?> themGioHangChiTiet(@RequestBody GioHangChiTietRequest gioHangChiTietRequest) {
        return ApiResponse.builder()
                .data(gioHangChiTietService.themGioHangChiTiet(gioHangChiTietRequest))
                .message("them gio hang chi tiet thanh cong")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse xoaGioHangChiTiet(@PathVariable Long id) {
        return ApiResponse.builder()
                .data(gioHangChiTietService.xoaKhoiGioHang(id))
                .message("xoa gio hang chi tiet thanh cong")
                .build();
    }

    @DeleteMapping("/san-pham-chi-tiet/{id}")
    public ApiResponse xoaGioHangChiTietBySanPhamChiTiet(@PathVariable(name = "id") Long idSpct, @RequestParam(name = "idGioHang") Long idGioHang) {
        return ApiResponse.builder()
                .data(gioHangChiTietService.xoaKhoiGioHangBySanPhamChiTiet(idSpct, idGioHang))
                .message("xoa gio hang chi tiet thanh cong")
                .build();
    }
    @PutMapping("/update")
    public ApiResponse updateGioHangChiTiet(@RequestParam(name = "idSanPhamChiTiet") Long idSanPhamChiTiet, @RequestParam(name = "idGioHang") Long idGioHang, @RequestParam(name = "soLuong") int soLuong) {
        gioHangChiTietService.updateGioHangChiTiet(idSanPhamChiTiet, idGioHang, soLuong);
        return ApiResponse.builder()
                .message("update successfull")
                .build();
    }

    @DeleteMapping("/delete-all")
    public ApiResponse xoaHetGioHang(@RequestParam(name = "idGioHang") Long idGioHang) {
        gioHangChiTietService.xoaHetGioHang(idGioHang);
        return ApiResponse.builder()
                .message("delete all successfull")
                .build();
    }

}
