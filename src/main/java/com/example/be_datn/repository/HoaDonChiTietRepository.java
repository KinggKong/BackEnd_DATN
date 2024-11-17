package com.example.be_datn.repository;

import com.example.be_datn.entity.HoaDonCT;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HoaDonChiTietRepository extends JpaRepository<HoaDonCT, Long> {
    List<HoaDonCT> findByHoaDon_Id(Long id);
}
