package com.example.be_datn.controller;

import com.example.be_datn.dto.Request.SanPhamRequest;
import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Response.SanPhamResponse;
import com.example.be_datn.service.impl.SanPhamService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/sanphams")
public class SanPhamController {
    SanPhamService sanPhamService;

    @GetMapping("")
    public ApiResponse<?> getSanPhams(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize));
        ApiResponse<Page<SanPhamResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(sanPhamService.getAllPageable(pageable));
        return apiResponse;
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getSanPhamById(@PathVariable("id") Long id) {
        ApiResponse<SanPhamResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(sanPhamService.getById(id));
        return apiResponse;
    }

    @PostMapping("")
    public ApiResponse<?> createSanPham(@RequestBody @Valid SanPhamRequest request) {
        ApiResponse<SanPhamResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(sanPhamService.create(request));
        apiResponse.setMessage("Thêm mới sản phẩm thành công");
        return apiResponse;
    }

    @PutMapping("/{id}")
    public ApiResponse<?> updateSanPham(@PathVariable("id") Long id, @RequestBody SanPhamRequest sanPhamRequest) {
        ApiResponse<SanPhamResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(sanPhamService.update(sanPhamRequest, id));
        apiResponse.setMessage("Sửa sản phẩm thành công");
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteSanPham(@PathVariable("id") Long id) {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(sanPhamService.delete(id));
        return apiResponse;
    }

}
