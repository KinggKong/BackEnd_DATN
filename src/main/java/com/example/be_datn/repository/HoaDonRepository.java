package com.example.be_datn.repository;

import com.example.be_datn.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {
    HoaDon findByMaHoaDon(String maHoaDon);

    @Query("SELECT hd FROM HoaDon hd WHERE hd.trangThai LIKE CONCAT('%', :trangThai, '%')  AND hd.trangThai <> 'PENDING' order by hd.created_at desc")
    List<HoaDon> findByTrangThai(@Param("trangThai") String trangThai);

}
