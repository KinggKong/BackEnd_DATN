package com.example.be_datn.repository;

import com.example.be_datn.entity.LichSuThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LichSuThanhToanRepository extends JpaRepository<LichSuThanhToan, Long> {
    @Query(value = "SELECT * FROM lich_su_thanh_toan WHERE hoa_don_id = :hoaDonId", nativeQuery = true)
    List<LichSuThanhToan> findByHoaDonIdNative(Long hoaDonId);
}
