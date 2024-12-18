package com.example.be_datn.service;

import com.example.be_datn.dto.Request.HoaDonUpdateRequest;
import com.example.be_datn.dto.Request.KhachHangRequest;
import com.example.be_datn.dto.Response.HoaDonCTResponse;
import com.example.be_datn.dto.Response.HoaDonChiTietResponse;
import com.example.be_datn.dto.Response.HoaDonResponse;
import com.example.be_datn.dto.Response.InfoGiaoHang;
import com.example.be_datn.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IHoaDonService {
    Page<HoaDonResponse> getAllHoaDon(Pageable pageable);
    HoaDonResponse getHoaDonById(Long id);
    Long createHoaDon();
    String deleteHoaDon(Long id);
    String updateHoaDon(Long id, HoaDonUpdateRequest request);
    String updateCustomer(Long idHoaDon, Long idKhachHang);
    String completeHoaDon(Long id, String method,String diaChi, Double tienShip,String tenNguoiNhan, String sdt, String ghiChu);
    HoaDonResponse findByMaHoaDon(String maHoaDon);
    void changeTypeBill(Long idHoaDon);
}
