package com.example.be_datn.service;

import com.example.be_datn.dto.Request.HoaDonChiTietRequest;
import com.example.be_datn.dto.Request.HoaDonChiTietUpdateRequest;
import com.example.be_datn.dto.Response.HoaDonChiTietResponse;
import com.example.be_datn.dto.Response.HoaDonResponse;
import com.example.be_datn.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IHoaDonChiTietService {
    Page<HoaDonChiTietResponse> getAllHoaDonChitiet(Pageable pageable);
    List<HoaDonChiTietResponse> getHoaDonChiTietByHoaDonId(Long hoaDonId);
    String create(HoaDonChiTietRequest request);
    HoaDon deleteHoaDonChiTiet(Long id);
    HoaDonChiTietResponse update(HoaDonChiTietUpdateRequest request, Long id);
}
