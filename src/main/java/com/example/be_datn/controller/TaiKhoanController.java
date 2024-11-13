package com.example.be_datn.controller;

import com.example.be_datn.dto.Request.TaiKhoanRequest;
import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Response.TaiKhoanResponse;
import com.example.be_datn.service.impl.TaiKhoanService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/taikhoans")
@Slf4j
public class TaiKhoanController {
    TaiKhoanService taiKhoanService;

    @GetMapping("")
    public ApiResponse<List<TaiKhoanResponse>> getAllTaiKhoan() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("username: {}", authentication.getName());
//        authentication.getAuthorities().forEach(grantedAuthority -> log.info("grantedAuthority: {}", grantedAuthority));
        log.info("role: {}", authentication.getAuthorities());
        ApiResponse<List<TaiKhoanResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Get all successfully");
        apiResponse.setData(taiKhoanService.getAllTaiKhoan());
        return apiResponse;
    }

    @GetMapping("/{id}")
    public ApiResponse<TaiKhoanResponse> getTaiKhoanById(@PathVariable Long id) {
        ApiResponse<TaiKhoanResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Get successfully");
        apiResponse.setData(taiKhoanService.getTaiKhoan(id));
        return apiResponse;
    }

    @PostMapping("")
    public ApiResponse<TaiKhoanResponse> addTaiKhoan(@RequestBody @Valid TaiKhoanRequest request) {
        ApiResponse<TaiKhoanResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Add successfully");
        apiResponse.setData(taiKhoanService.createTaiKhoan(request));
        return apiResponse;
    }

    @PutMapping("/{id}")
    public ApiResponse<TaiKhoanResponse> updateTaiKhoan(@PathVariable Long id, @RequestBody TaiKhoanRequest request) {
        ApiResponse<TaiKhoanResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Update successfully");
        apiResponse.setData(taiKhoanService.updateTaiKhoan(request, id));
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteTaiKhoan(@PathVariable Long id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Delete successfully");
        return apiResponse;
    }

}
