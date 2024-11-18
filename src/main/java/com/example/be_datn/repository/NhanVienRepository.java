package com.example.be_datn.repository;

import com.example.be_datn.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, Long> {
    Optional<NhanVien> findByTen(String ten);
}
