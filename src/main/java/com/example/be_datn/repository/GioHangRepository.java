package com.example.be_datn.repository;

import com.example.be_datn.entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GioHangRepository extends JpaRepository<GioHang, Long> {
    GioHang findByKhachHang_Id(Long id);
}
