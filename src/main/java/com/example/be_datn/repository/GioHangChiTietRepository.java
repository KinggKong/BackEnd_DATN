package com.example.be_datn.repository;

import com.example.be_datn.entity.GioHang;
import com.example.be_datn.entity.GioHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GioHangChiTietRepository extends JpaRepository<GioHangChiTiet, Long> {
    List<GioHangChiTiet> findByGioHang_Id(Long id);


    @Query(value = "select ghct from GioHangChiTiet ghct where ghct.gioHang.id = :idGioHang and ghct.sanPhamChiTiet.id = :idSanPhamChiTiet")
    GioHangChiTiet findByIdGiohangAndIdSanPhamChiTiet(@Param("idGioHang") Long idGiohang,@Param("idSanPhamChiTiet") Long idSanPhamChiTiet);
}
