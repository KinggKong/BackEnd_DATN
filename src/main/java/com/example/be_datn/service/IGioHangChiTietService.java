package com.example.be_datn.service;

import com.example.be_datn.dto.Request.GioHangChiTietRequest;
import com.example.be_datn.dto.Response.GioHangChiTietResponse;

import java.util.List;

public interface IGioHangChiTietService {
    List<GioHangChiTietResponse> findByIdGioHang(Long idGioHang);

    GioHangChiTietResponse themGioHangChiTiet(GioHangChiTietRequest gioHangChiTietRequest);

    int xoaKhoiGioHang(Long idGioHangChiTiet);
}
