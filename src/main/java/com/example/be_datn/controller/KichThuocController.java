package com.example.be_datn.controller;

import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Request.KichThuocRequest;
import com.example.be_datn.dto.Response.KichThuocResponse;
import com.example.be_datn.service.IKichThuocService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/kichthuocs")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class KichThuocController {
    IKichThuocService kichThuocService;

    @GetMapping("")
    ApiResponse<Page<KichThuocResponse>> getAllKichThuocs(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                                          @RequestParam(name = "tenKichThuoc", defaultValue = "") String tenKichThuoc
    ) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize));
        ApiResponse<Page<KichThuocResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(kichThuocService.getAllKichThuocPageable(tenKichThuoc, pageable));
        return apiResponse;
    }

    @PostMapping("")
    ApiResponse<KichThuocResponse> createKichThuoc(@RequestBody @Valid KichThuocRequest kichThuocRequest) {
        ApiResponse<KichThuocResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Thêm mới thành công kích thước " + kichThuocRequest.getTenKichThuoc());
        apiResponse.setData(kichThuocService.createKichThuoc(kichThuocRequest));
        return apiResponse;
    }

    @GetMapping("/{id}")
    ApiResponse<KichThuocResponse> getKichThuocById(@PathVariable Long id) {
        ApiResponse<KichThuocResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(kichThuocService.getKichThuocById(id));
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteKichThuocById(@PathVariable Long id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(kichThuocService.deleteKichThuoc(id));
        return apiResponse;
    }

    @PutMapping("/{id}")
    ApiResponse<KichThuocResponse> updateKichThuoc(@PathVariable Long id, @RequestBody @Valid KichThuocRequest kichThuocRequest) {
        ApiResponse<KichThuocResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Cập nhật thành công kích thước");
        apiResponse.setData(kichThuocService.updateKichThuoc(id, kichThuocRequest));
        return apiResponse;
    }
}
