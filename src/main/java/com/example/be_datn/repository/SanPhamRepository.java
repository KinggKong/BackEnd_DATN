package com.example.be_datn.repository;

import com.example.be_datn.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Long>, JpaSpecificationExecutor<SanPham> {
    boolean existsByTenSanPham(String tenSanPham);
    List<SanPham> getAllByTenSanPhamContainingIgnoreCase(String tenSanPham);
    @Query(value = "SELECT sp.id, sp.ten_san_pham,sp.mo_ta,sp.trang_thai, sp.id_danh_muc," +
            "sp.id_thuong_hieu, sp.id_chat_lieu_vai, sp.id_chat_lieu_de, sp.created_at, sp.updated_at" +
            " FROM san_pham sp \n" +
            "where (sp.id_danh_muc = '' or sp.id_danh_muc = :idDanhMuc or :idDanhMuc is null) \n" +
            "and (sp.id_thuong_hieu = '' or sp.id_thuong_hieu = :idThuongHieu or :idThuongHieu is null)\n" +
            "and (sp.id_chat_lieu_de = '' or sp.id_chat_lieu_de = :idChatLieuDe or :idChatLieuDe is null)\n" +
            "and (sp.id_chat_lieu_vai = '' or sp.id_chat_lieu_vai = :idChatLieuVai or :idChatLieuVai is null)\n" +
            "and (sp.ten_san_pham like :tenSanPham)", nativeQuery = true)
    Page<SanPham> getAllByFilter(@Param("idDanhMuc") Long idDanhMuc,
                                 @Param("idThuongHieu") Long idThuongHieu,
                                 @Param("idChatLieuVai") Long idChatLieuVai,
                                 @Param("idChatLieuDe") Long idChatLieuDe,
                                 @Param(("tenSanPham")) String tenSanPham,
                                 Pageable pageable);
    @Query(value = "SELECT sp.* FROM san_pham sp \n" +
            "where sp.trang_thai = 1" , nativeQuery = true)
    Page<SanPham> getAllByFilterCustumer(
                                 Pageable pageable);
    @Query(value = "SELECT sp.* FROM san_pham sp \n" +
            "where sp.id_danh_muc = :idDanhMuc " +
            "and sp.trang_thai = 1", nativeQuery = true)
    List<SanPham> getSanPhamByDanhMucID(@Param("idDanhMuc") Integer id);


    //Loc ben khach hang
    @Query(value = "SELECT sp.* FROM san_pham sp " +
            "WHERE (   :idDanhMuc IS NULL OR sp.id_danh_muc IN :idDanhMuc ) " +
            "AND (sp.id_thuong_hieu = '' OR sp.id_thuong_hieu = :idThuongHieu OR :idThuongHieu IS NULL) " +
            "AND ( :idChatLieuVai IS NULL or sp.id_chat_lieu_vai IN :idChatLieuVai ) " +
            "AND (:idChatLieuDe IS NULL  or sp.id_chat_lieu_de IN :idChatLieuDe ) " +
            "AND (sp.ten_san_pham LIKE CONCAT('%', :tenSanPham, '%'))", nativeQuery = true)
    Page<SanPham> getAllByFilterCustumers(@Param("idDanhMuc") List<Long> idDanhMuc,
                                          @Param("idThuongHieu") Long idThuongHieu,
                                          @Param("idChatLieuVai") List<Long> idChatLieuVai,
                                          @Param("idChatLieuDe") List<Long> idChatLieuDe,
                                          @Param("tenSanPham") String tenSanPham,
                                          Pageable pageable);

    //Lấy ra danh sách sản phẩm bán chạy
    @Query(value = "SELECT sp.id, sp.ten_san_pham, MIN(spct.gia_tien), MAX(spct.gia_tien), SUM(hdct.so_luong) AS totalQuantity " +
            "FROM hoa_don_ct hdct " +
            "JOIN san_pham_chi_tiet spct ON hdct.id_san_pham_ct = spct.id " +
            "JOIN san_pham sp ON spct.id_san_pham = sp.id " +
            "JOIN hoa_don hd ON hdct.id_hoa_don = hd.id " +
            "WHERE hdct.trang_thai = 1 " +
            "AND hd.trang_thai = 'DONE' " +
            "GROUP BY sp.id, sp.ten_san_pham " +
            "ORDER BY totalQuantity DESC", nativeQuery = true)
    List<Object[]> findBestSellingProductsNative();


}
