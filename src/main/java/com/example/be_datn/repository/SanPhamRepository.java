package com.example.be_datn.repository;

import com.example.be_datn.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SanPhamRepository extends JpaRepository<SanPham, Long> {
    boolean existsByTenSanPham(String tenSanPham);
}
