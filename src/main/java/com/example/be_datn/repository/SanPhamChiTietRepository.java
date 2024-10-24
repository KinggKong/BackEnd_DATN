package com.example.be_datn.repository;

import com.example.be_datn.entity.SanPham;
import com.example.be_datn.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet,Long> {
    @Query(value = "select * from san_pham_chi_tiet where id_san_pham = ?1 and id_mau_sac = ?2 and id_kich_thuoc = ?3", nativeQuery = true)
    SanPhamChiTiet findBySanPhamIdAndMauSacIdAndKichThuocId(Long idSanPham, Long idMauSac, Long idKichThuoc);
    @Query("select spct from SanPhamChiTiet spct join SanPham sp on spct.sanPham.id = sp.id where sp.id = ?1 and spct.trangThai = 1")
    Page<SanPhamChiTiet> findBySanPhamId(Long id, Pageable pageable);
    @Query("select spct from SanPhamChiTiet spct where spct.sanPham.id = ?1")
    Page<SanPhamChiTiet> findSPCTBySanPhamId(Long id, Pageable pageable);

    @Query(value = "SELECT spct.* FROM san_pham_chi_tiet spct join san_pham sp " +
            "on spct.id_san_pham = sp.id " +
            "where (sp.id_danh_muc = '' or sp.id_danh_muc = :idDanhMuc or :idDanhMuc is null) " +
            "and (sp.id_thuong_hieu = '' or sp.id_thuong_hieu = :idThuongHieu or :idThuongHieu is null) " +
            "and (sp.id_chat_lieu_de = '' or sp.id_chat_lieu_de = :idChatLieuDe or :idChatLieuDe is null) " +
            "and (sp.id_chat_lieu_vai = '' or sp.id_chat_lieu_vai = :idChatLieuVai or :idChatLieuVai is null) " +
            "AND (sp.id = '' OR sp.id = :idSanPham OR :idSanPham IS NULL)", nativeQuery = true)
    Page<SanPhamChiTiet> getAllByFilter(@Param("idDanhMuc") Long idDanhMuc,
                                 @Param("idThuongHieu") Long idThuongHieu,
                                 @Param("idChatLieuVai") Long idChatLieuVai,
                                 @Param("idChatLieuDe") Long idChatLieuDe,
                                 @Param(("idSanPham")) Long idSanPham,
                                 Pageable pageable);
//    Page<SanPhamChiTiet> getAll(Pageable pageable);
}
