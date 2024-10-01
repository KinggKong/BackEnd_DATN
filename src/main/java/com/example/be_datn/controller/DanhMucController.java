package com.example.be_datn.controller;

import com.example.be_datn.dto.ApiResponse;
import com.example.be_datn.dto.request.DanhMucCreationRequest;
import com.example.be_datn.dto.request.DanhMucUpdateRequest;
import com.example.be_datn.dto.response.DanhMucResponse;
import com.example.be_datn.entity.DanhMuc;
import com.example.be_datn.service.IDanhMucService;
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
@RequestMapping("/api/v1/danhMuc")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class DanhMucController {
    IDanhMucService danhMucService;

    @GetMapping("")
    ApiResponse<Page<DanhMuc>> getAllDanhMucs(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                              @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                              @RequestParam(name = "tenDanhMuc",defaultValue = "") String tenDanhMuc
    ) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize));
        ApiResponse<Page<DanhMuc>> apiResponse = new ApiResponse<>();
        apiResponse.setData(danhMucService.getAllDanhMucPageable(tenDanhMuc,pageable));
        return apiResponse;
    }

    @PostMapping("")
    ApiResponse<DanhMucResponse> createDanhMuc(@RequestBody @Valid DanhMucCreationRequest danhMuc) {
        ApiResponse<DanhMucResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Thêm mới thành công danh mục " + danhMuc.getTenDanhMuc());
        apiResponse.setData(danhMucService.createDanhMuc(danhMuc));
        return apiResponse;
    }

    @GetMapping("/{id}")
    ApiResponse<DanhMucResponse> getDanhMucById(@PathVariable Long id) {
        ApiResponse<DanhMucResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(danhMucService.getDanhMucById(id));
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteDanhMucById(@PathVariable Long id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(danhMucService.deleteDanhMuc(id));
        return apiResponse;
    }

    @PutMapping("/{id}")
    ApiResponse<DanhMucResponse> updateDanhMuc(@PathVariable Long id, @RequestBody @Valid DanhMucUpdateRequest danhMuc) {
        ApiResponse<DanhMucResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Cập nhật thành công danh mục");
        apiResponse.setData(danhMucService.updateDanhMuc(id, danhMuc));
        return apiResponse;
    }

}