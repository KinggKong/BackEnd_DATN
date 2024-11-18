package com.example.be_datn.service;

import com.example.be_datn.dto.Request.NhanVienRequest;
import com.example.be_datn.dto.Request.NhanVienUpdate;
import com.example.be_datn.dto.Response.NhanVienResponse;
import com.example.be_datn.entity.NhanVien;
import org.springframework.data.domain.Page;

public interface NhanVienService {
    Page<NhanVienResponse> gets(int page, int size);
    void create(NhanVienRequest request);
    void delete(Long id);
    NhanVien get(Long id);
    void update(NhanVienUpdate request, Long id);
}