package com.example.be_datn.service;

import com.example.be_datn.dto.Request.KhachHangRequest1;
import com.example.be_datn.dto.Response.KhachHangResponse1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IKhachHangService {

    // Lấy danh sách khách hàng với phân trang và tìm kiếm theo tên
    Page<KhachHangResponse1> getAllKhachHangPageable(String ten, Pageable pageable);

    // Tạo mới khách hàng
    KhachHangResponse1 createKhachHang(KhachHangRequest1 khachHangRequest);

    // Lấy thông tin khách hàng theo ID
    KhachHangResponse1 getKhachHangById(Long id);

    // Cập nhật thông tin khách hàng theo ID
    KhachHangResponse1 updateKhachHang(Long id, KhachHangRequest1 khachHangRequest);

    // Xóa khách hàng theo ID
    String deleteKhachHang(Long id);
}
