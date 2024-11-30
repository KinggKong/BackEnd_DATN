package com.example.be_datn.service;

import com.example.be_datn.dto.Response.HoaDonResponse;

public interface IHoaDonService {
    HoaDonResponse findByMaHoaDon(String maHoaDon);
}
