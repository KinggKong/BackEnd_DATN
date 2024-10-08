package com.example.be_datn.repository;

import com.example.be_datn.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SanPhamRepository extends JpaRepository<SanPham, Long> {
    boolean existsByTenSanPham(String tenSanPham);

    @Query(value = "SELECT sp.* FROM san_pham sp \n" +
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
}
