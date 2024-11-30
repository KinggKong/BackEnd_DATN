package com.example.be_datn.repository;

import com.example.be_datn.entity.LichSuThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LichSuThanhToanRepository extends JpaRepository<LichSuThanhToan, Long> {
    @Query(
            "select lstt from LichSuThanhToan  lstt where lstt.hoaDon.id =:hoaDonId"
    )
    LichSuThanhToan findByHoaDonId(Long hoaDonId);
}
