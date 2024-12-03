package com.example.be_datn.repository;

import com.example.be_datn.entity.LichSuThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LichSuThanhToanRepository extends JpaRepository<LichSuThanhToan, Long> {
    LichSuThanhToan findByHoaDon_Id(Long idHoaDon);
    void deleteByHoaDon_Id(Long idHoaDon);
}
