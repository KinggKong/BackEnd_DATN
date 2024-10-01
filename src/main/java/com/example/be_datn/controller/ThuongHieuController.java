package com.example.be_datn.controller;

import com.example.be_datn.dto.ApiResponse;
import com.example.be_datn.dto.Request.ThuongHieuCreationRequest;
import com.example.be_datn.dto.Request.ThuongHieuUpdateRequest;
import com.example.be_datn.dto.response.ThuongHieuResponse;
import com.example.be_datn.entity.ThuongHieu;
import com.example.be_datn.mapper.ThuongHieuMapper;
import com.example.be_datn.service.IThuongHieuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/thuongHieu")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ThuongHieuController {
    IThuongHieuService thuongHieuService;

    private final ThuongHieuMapper thuongHieuMapper;
    @GetMapping("")
    ApiResponse<Page<ThuongHieu>> getAllThuongHieu(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                                   @RequestParam(name = "tenThuongHieu",defaultValue = "") String tenThuongHieu
    ) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize));
        ApiResponse<Page<ThuongHieu>> apiResponse = new ApiResponse<>();
        apiResponse.setData(thuongHieuService.getAllThuongHieuPageable(tenThuongHieu,pageable));
        return apiResponse;
    }

    @PostMapping("")
    ApiResponse<?> createThuongHieu(@RequestBody @Valid ThuongHieuCreationRequest request) {
        ApiResponse<ThuongHieuResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Thêm mới thành thương hiệu " + request.getTenThuongHieu());
        apiResponse.setData(thuongHieuService.createThuongHieu(request));
        return apiResponse;
    }

    @GetMapping("/{id}")
    ApiResponse<ThuongHieuResponse> getThuongHieuById(@PathVariable Long id) {
        ApiResponse<ThuongHieuResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(thuongHieuService.getThuongHieuById(id));
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteThuongHieuById(@PathVariable Long id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(thuongHieuService.deleteThuongHieu(id));
        return apiResponse;
    }

    @PutMapping("/{id}")
    ApiResponse<ThuongHieuResponse> updateThuongHieu(@PathVariable Long id, @RequestBody @Valid ThuongHieuUpdateRequest request) {
        ApiResponse<ThuongHieuResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Cập nhật thành công thương hiệu");
        apiResponse.setData(thuongHieuService.updateThuongHieu(id, request));
        return apiResponse;
    }

}