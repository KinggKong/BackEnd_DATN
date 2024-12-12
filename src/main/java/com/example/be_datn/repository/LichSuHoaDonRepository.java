package com.example.be_datn.repository;

import com.example.be_datn.entity.HoaDon;
import com.example.be_datn.entity.LichSuHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LichSuHoaDonRepository extends JpaRepository<LichSuHoaDon, Long> {
    List<LichSuHoaDon> findByHoaDon_Id(Long id);


    @Query("""
select lshd from LichSuHoaDon lshd where lshd.hoaDon.id=?1
""")
    LichSuHoaDon findLichSuHoaDonByHoaDonId(Long id);
    void deleteByHoaDon_Id(Long id);
}
