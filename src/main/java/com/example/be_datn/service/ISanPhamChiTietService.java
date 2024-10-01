package com.example.be_datn.service;

import com.example.be_datn.dto.Request.SanPhamChiTietRequest;
import com.example.be_datn.dto.Response.SanPhamChiTietResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISanPhamChiTietService {
    Page<SanPhamChiTietResponse> getAllPage(Pageable pageable);
    Page<SanPhamChiTietResponse> getAllPageBySanPhamId(Long id, Pageable pageable);
    SanPhamChiTietResponse create(SanPhamChiTietRequest sanPhamChiTietRequest);
    SanPhamChiTietResponse update(Long id, SanPhamChiTietRequest sanPhamChiTietRequest);
    SanPhamChiTietResponse getById(Long id);
    String delete(Long id);

}
