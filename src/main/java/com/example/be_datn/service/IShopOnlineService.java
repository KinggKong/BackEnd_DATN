package com.example.be_datn.service;

import com.example.be_datn.dto.Request.HoaDonRequest;
import com.example.be_datn.dto.Response.AboutProductShopOn;
import com.example.be_datn.dto.Response.DetailHistoryBillResponse;
import com.example.be_datn.dto.Response.HoaDonResponse;
import com.example.be_datn.dto.Response.InfoOrder;

import java.util.List;

public interface IShopOnlineService {
    AboutProductShopOn preCheckout(Long idKhachHang);

    HoaDonResponse checkout(HoaDonRequest hoaDonRequest);

    InfoOrder getInfoOrder(String maHoaDon);

    List<HoaDonResponse> getAllOrderByStatus(String trangThai);

    DetailHistoryBillResponse getDetailHistoryBill(Long idHoaDon);


}
