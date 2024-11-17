package com.example.be_datn.repository;

import com.example.be_datn.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Long> {
    boolean existsTaiKhoanByMa(String ma);

    Optional<TaiKhoan> findByTenDangNhap(String tenDangNhap);
}
