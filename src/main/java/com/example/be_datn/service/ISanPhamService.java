package com.example.be_datn.service;

import com.example.be_datn.dto.Request.SanPhamRequest;
import com.example.be_datn.dto.Response.SanPhamResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISanPhamService {
    Page<SanPhamResponse> getAllPageable(Pageable pageable);

    SanPhamResponse getById(Long id);

    SanPhamResponse create(SanPhamRequest sanPhamRequest);

    SanPhamResponse update(SanPhamRequest sanPhamRequest, Long id);

    String delete(Long id);
}
