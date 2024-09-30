package com.example.be_datn.controller;

import com.example.be_datn.dto.Request.MauSacRequest;
import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Response.MauSacResponse;
import com.example.be_datn.entity.MauSac;
import com.example.be_datn.service.IMauSacService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mausacs")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class MauSacController {
    IMauSacService mauSacService;

    @GetMapping("")
    ApiResponse<Page<MauSacResponse>> getAllMauSacs(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                            @RequestParam(name = "tenMau",defaultValue = "") String ten_mau
    ) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize));
        ApiResponse<Page<MauSacResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(mauSacService.getAllMauSacPageable(ten_mau,pageable));
        return apiResponse;
    }

    @PostMapping("")
    ApiResponse<MauSacResponse> createMauSac(@RequestBody @Valid MauSacRequest mauSacRequest) {
        ApiResponse<MauSacResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Thêm mới thành công màu " + mauSacRequest.getTenMau());
        apiResponse.setData(mauSacService.createMauSac(mauSacRequest));
        return apiResponse;
    }

    @GetMapping("/{id}")
    ApiResponse<MauSacResponse> getMauSacById(@PathVariable Long id) {
        ApiResponse<MauSacResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(mauSacService.getMauSacById(id));
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteMauSacById(@PathVariable Long id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(mauSacService.deleteMauSac(id));
        return apiResponse;
    }

    @PutMapping("/{id}")
    ApiResponse<MauSacResponse> updateMauSac(@PathVariable Long id, @RequestBody @Valid MauSacRequest mauSacRequest) {
        ApiResponse<MauSacResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Cập nhật thành công màu sắc");
        apiResponse.setData(mauSacService.updateMauSac(id, mauSacRequest));
        return apiResponse;
    }

}
