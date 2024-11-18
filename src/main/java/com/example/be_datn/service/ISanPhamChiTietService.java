package com.example.be_datn.service;

import com.example.be_datn.dto.Request.SanPhamChiTietRequest;
import com.example.be_datn.dto.Response.SanPhamChiTietResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface ISanPhamChiTietService {
    Page<SanPhamChiTietResponse> getAllPage(Pageable pageable);
    Page<SanPhamChiTietResponse> getAllPageBySanPhamId(Long id, Pageable pageable);
    List<SanPhamChiTietResponse> create(List<SanPhamChiTietRequest> sanPhamChiTietRequest) throws IOException;
    SanPhamChiTietResponse update(Long id, SanPhamChiTietRequest sanPhamChiTietRequest);
    SanPhamChiTietResponse getById(Long id);
    String delete(Long id);
    void updateSoLuongSanPhamChiTiet(Long id, Integer soLuong, String method);

}
