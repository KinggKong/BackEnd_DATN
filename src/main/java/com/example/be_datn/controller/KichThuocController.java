package com.example.be_datn.controller;

import com.example.be_datn.dto.ApiResponse;
import com.example.be_datn.entity.KichThuoc;
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
    ApiResponse<Page<KichThuoc>> getAllKichThuocs(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                                  @RequestParam(name = "tenKichThuoc", defaultValue = "") String tenKichThuoc // Thay đổi tên tham số
    ) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize));
        ApiResponse<Page<KichThuoc>> apiResponse = new ApiResponse<>();
        apiResponse.setData(kichThuocService.getAllKichThuocPageable(tenKichThuoc, pageable));
        return apiResponse;
    }

    @PostMapping("")
    ApiResponse<KichThuoc> createKichThuoc(@RequestBody @Valid KichThuoc kichThuoc) {
        ApiResponse<KichThuoc> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Thêm mới thành công kích thước " + kichThuoc.getTenKichThuoc());
        apiResponse.setData(kichThuocService.createKichThuoc(kichThuoc));
        return apiResponse;
    }

    @GetMapping("/{id}")
    ApiResponse<KichThuoc> getKichThuocById(@PathVariable Long id) {
        ApiResponse<KichThuoc> apiResponse = new ApiResponse<>();
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
    ApiResponse<KichThuoc> updateKichThuoc(@PathVariable Long id, @RequestBody @Valid KichThuoc kichThuoc) {
        ApiResponse<KichThuoc> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Cập nhật thành công kích thước");
        apiResponse.setData(kichThuocService.updateKichThuoc(id, kichThuoc));
        return apiResponse;
    }
}