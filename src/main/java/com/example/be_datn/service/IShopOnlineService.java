package com.example.be_datn.service;

import com.example.be_datn.dto.Request.HoaDonRequest;
import com.example.be_datn.dto.Response.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IShopOnlineService {
    AboutProductShopOn preCheckout(Long idKhachHang);

    HoaDonResponse checkout(HoaDonRequest hoaDonRequest);

    InfoOrder getInfoOrder(String maHoaDon);

    List<HoaDonResponse> getAllOrderByStatus(String trangThai);

    DetailHistoryBillResponse getDetailHistoryBill(Long idHoaDon);

    Page<HistoryBillResponse> getAllHistoryBill(Long idKhachHang,int pageNumber,int pageSize);
}
