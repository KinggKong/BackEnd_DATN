package com.example.be_datn.repository;

import com.example.be_datn.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {
    HoaDon findByMaHoaDon(String maHoaDon);

    @Query("SELECT hd FROM HoaDon hd WHERE hd.trangThai LIKE CONCAT('%', :trangThai, '%') order by hd.created_at desc")
    List<HoaDon> findByTrangThai(@Param("trangThai") String trangThai);

    @Query("select count(hdb) from HoaDon hdb where hdb.trangThai = ?1 and hdb.created_at >= ?2 and hdb.created_at <= ?3")
    int countByTrangThai(String trangThai, Instant ngayBatDau, Instant ngayKetThuc);

    @Query("select sum(hdb.tongTien) from HoaDon hdb where hdb.trangThai='DONE' and hdb.created_at >= ?1 and hdb.created_at <= ?2")
    Float tongDoanhThu(Instant ngayBatDau, Instant ngayKetThuc);

    @Query("select sum(hdct.soLuong) from HoaDonCT hdct join HoaDon hd on hdct.hoaDon.id = hd.id where hd.trangThai ='DONE' and  hd.created_at >= ?1 and hd.created_at <= ?2")
    Integer tongSanPhamBan( Instant ngayBatDau, Instant ngayKetThuc);

}
