package com.example.be_datn.service;

import com.example.be_datn.dto.Request.HoaDonChiTietRequest;
import com.example.be_datn.dto.Request.HoaDonChiTietUpdateRequest;
import com.example.be_datn.dto.Response.HoaDonCTResponse;
import com.example.be_datn.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IHoaDonChiTietService {
//    Page<HoaDonCTResponse> getAllHoaDonChitiet(Pageable pageable);
//    List<HoaDonCTResponse> getHoaDonChiTietByHoaDonId(Long hoaDonId);
    String create(HoaDonChiTietRequest request);
    String deleteHoaDonChiTiet(Long id);
    HoaDonCTResponse update(HoaDonChiTietUpdateRequest request, Long id);
    Page<HoaDonCTResponse> getAllHdct(Pageable pageable, Long idHoaDonCT);
    List<HoaDonCTResponse> getAllHdctByIdHoaDon(Long hoaDonId);
}
