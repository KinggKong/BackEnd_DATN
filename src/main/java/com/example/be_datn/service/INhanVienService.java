package com.example.be_datn.service;

import com.example.be_datn.dto.Request.NhanVienRequest;
import com.example.be_datn.dto.Response.NhanVienResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface INhanVienService {

    // Lấy danh sách nhân viên với phân trang và tìm kiếm theo tên
    Page<NhanVienResponse> getAllNhanVienPageable(String ten, Pageable pageable);

    // Tạo mới nhân viên
    NhanVienResponse createNhanVien(NhanVienRequest nhanVienRequest);

    // Lấy thông tin nhân viên theo ID
    NhanVienResponse getNhanVienById(Long id);

    // Cập nhật thông tin nhân viên theo ID
    NhanVienResponse updateNhanVien(Long id, NhanVienRequest nhanVienRequest);

    // Xóa nhân viên theo ID
    String deleteNhanVien(Long id);
}