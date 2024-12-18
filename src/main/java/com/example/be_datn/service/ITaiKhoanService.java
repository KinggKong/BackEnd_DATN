package com.example.be_datn.service;

import com.example.be_datn.dto.Request.TaiKhoanRequest;
import com.example.be_datn.dto.Response.TaiKhoanResponse;

import java.util.List;

public interface ITaiKhoanService {
    List<TaiKhoanResponse> getAllTaiKhoan();

    TaiKhoanResponse createTaiKhoan(TaiKhoanRequest taiKhoanRequest);

    TaiKhoanResponse getTaiKhoan(Long taiKhoanId);

    String deleteTaiKhoan(Long taiKhoanId);

    TaiKhoanResponse updateTaiKhoan(TaiKhoanRequest taiKhoanRequest, Long taiKhoanId);

    boolean existTaiKhoan(String maTaiKhoan);

    boolean existTenTaiKhoan(String tenTaiKhoan);
    TaiKhoanResponse getTaiKhoanByIDOwner(String email);
}
