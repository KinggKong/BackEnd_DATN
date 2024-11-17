package com.example.be_datn.service;

import com.example.be_datn.dto.Request.StatusBillRequest;
import com.example.be_datn.dto.Response.LichSuHoaDonResponse;

import java.util.List;

public interface ILichSuHoaDonService {

    LichSuHoaDonResponse insertLichSuHoaDon(StatusBillRequest statusBillRequest);

    List<LichSuHoaDonResponse> findLichSuHoaDonByIdHoaDon(Long idHoaDon);
}
