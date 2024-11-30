package com.example.be_datn.service;

import com.example.be_datn.dto.Request.SaleRequest;
import com.example.be_datn.dto.Response.SaleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ISaleService {
//    Page<SaleResponse> getAll(String tenChienDich, String ngayBatDau, String ngayKetthuc, int trangThai, Pageable pageable);
//    Page<SaleResponse> getAll(Pageable pageable);
    Page<SaleResponse> getAll(String tenChienDich, java.time.LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, Integer trangThai, Pageable pageable);
    SaleResponse getById(Long id);
    SaleResponse create(SaleRequest saleRequest);
    SaleResponse update(SaleRequest saleRequest, Long id);
    String delete(Long id);
    SaleResponse updateStatus(Long id, int status);
    List<String> getAllTenChienDich();
}
