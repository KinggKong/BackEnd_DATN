package com.example.be_datn.controller;


import com.example.be_datn.dto.Request.NhanVienUpdate;
import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Response.NhanVienResponse;
import com.example.be_datn.entity.NhanVien;
import com.example.be_datn.service.NhanVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/nhanVien")
public class NhanVienController {
    private final NhanVienService nhanVienService;

    @GetMapping
    public ApiResponse<Page<NhanVienResponse>> getNhanVien(
                                                           @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                           @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        ApiResponse<Page<NhanVienResponse>> response = new ApiResponse<>();
        response.setData(nhanVienService.gets(page, size));
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<NhanVien> getNhanVien(@PathVariable Long id) {
        ApiResponse<NhanVien> response = new ApiResponse<>();
        response.setData(nhanVienService.get(id));
        return response;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        nhanVienService.delete(id);
        return new ApiResponse<>();
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody NhanVienUpdate body) {
        nhanVienService.update(body, id);
        return new ApiResponse<>();
    }
}