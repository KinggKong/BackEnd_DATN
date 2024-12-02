package com.example.be_datn.repository;

import com.example.be_datn.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {
    KhachHang findByTen(String ten);
}
