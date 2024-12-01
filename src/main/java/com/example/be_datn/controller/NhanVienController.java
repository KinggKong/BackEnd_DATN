package com.example.be_datn.controller;

import com.example.be_datn.dto.Request.NhanVienRequest;
import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Response.NhanVienResponse;
import com.example.be_datn.service.INhanVienService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/nhanviens")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class NhanVienController {

    INhanVienService nhanVienService;

    // Lấy tất cả nhân viên với phân trang và tìm kiếm theo tên
    @GetMapping("")
    ApiResponse<Page<NhanVienResponse>> getAllNhanViens(
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "ten", defaultValue = "") String ten) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize));
        ApiResponse<Page<NhanVienResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(nhanVienService.getAllNhanVienPageable(ten, pageable));
        return apiResponse;
    }

    // Tạo mới nhân viên
    @PostMapping("")
    ApiResponse<NhanVienResponse> createNhanVien(@RequestBody @Valid NhanVienRequest nhanVienRequest) {
        ApiResponse<NhanVienResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Thêm mới thành công nhân viên " + nhanVienRequest.getTen());
        apiResponse.setData(nhanVienService.createNhanVien(nhanVienRequest));
        return apiResponse;
    }

    // Lấy thông tin chi tiết nhân viên theo ID
    @GetMapping("/{id}")
    ApiResponse<NhanVienResponse> getNhanVienById(@PathVariable Long id) {
        ApiResponse<NhanVienResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(nhanVienService.getNhanVienById(id));
        return apiResponse;
    }

    // Xóa nhân viên theo ID
    @DeleteMapping("/{id}")
    ApiResponse<String> deleteNhanVienById(@PathVariable Long id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(nhanVienService.deleteNhanVien(id));
        return apiResponse;
    }

    // Cập nhật thông tin nhân viên theo ID
    @PutMapping("/{id}")
    ApiResponse<NhanVienResponse> updateNhanVien(@PathVariable Long id, @RequestBody @Valid NhanVienRequest nhanVienRequest) {
        ApiResponse<NhanVienResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Cập nhật thành công nhân viên");
        apiResponse.setData(nhanVienService.updateNhanVien(id, nhanVienRequest));
        return apiResponse;
    }
}
