package com.example.be_datn.repository;

import com.example.be_datn.entity.GioHangChiTiet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface GioHangChiTietRepository extends JpaRepository<GioHangChiTiet, Long> {
    List<GioHangChiTiet> findByGioHang_Id(Long id);


    @Query(value = "select ghct from GioHangChiTiet ghct where ghct.gioHang.id = :idGioHang and ghct.sanPhamChiTiet.id = :idSanPhamChiTiet")
    GioHangChiTiet findByIdGiohangAndIdSanPhamChiTiet(@Param("idGioHang") Long idGiohang, @Param("idSanPhamChiTiet") Long idSanPhamChiTiet);

    //Xóa giỏ hàng chi tiết theo id sản phẩm chi tiết
    @Modifying
    @Query(value = "delete from GioHangChiTiet ghct where ghct.sanPhamChiTiet.id = :idSanPhamChiTiet and ghct.gioHang.id = :idGioHang")
    int deleteByIdSanPhamChiTiet(@Param("idSanPhamChiTiet") Long idSanPhamChiTiet, @Param("idGioHang") Long idGioHang);

    //Cập nhật số lượng thông qua id sản phẩm chi tiết và id giỏ hàng
    @Transactional
    @Modifying
    @Query(value = "UPDATE gio_hang_ct SET so_luong = :soLuong+so_luong WHERE id_gio_hang = :idGioHang AND id_san_pham_chi_tiet = :idSanPhamChiTiet", nativeQuery = true)
    void updateNativeQuantity(@Param("idSanPhamChiTiet") Long idSanPhamChiTiet,
                              @Param("idGioHang") Long idGioHang,
                              @Param("soLuong") int soLuong);


    //Lấy ra tất cả giỏ hàng chi tiết theo thoi giam giam gia het han
//    @Query(value = "select ghct from GioHangChiTiet ghct where ghct.thoiGianGiamGia < current_timestamp and ghct.trangThai = 1")
    List<GioHangChiTiet> findByThoiGianGiamGiaBefore(LocalDateTime localDateTime);

    @Modifying
    @Transactional
    @Query(value = "delete from gio_hang_ct where id_gio_hang = :idGioHang", nativeQuery = true)
    void deleteByGioHang_Id(@Param("idGioHang") Long id);


    List<GioHangChiTiet> findBySanPhamChiTietTrangThai(int trangThai);


}
