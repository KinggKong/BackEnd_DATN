package com.example.be_datn.repository;

import com.example.be_datn.entity.SanPham;
import com.example.be_datn.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet,Long> {
    @Query(value = "select * from san_pham_chi_tiet where id_san_pham = ?1 and id_mau_sac = ?2 and id_kich_thuoc = ?3", nativeQuery = true)
    SanPhamChiTiet findBySanPhamIdAndMauSacIdAndKichThuocId(Long idSanPham, Long idMauSac, Long idKichThuoc);
    @Query("select spct from SanPhamChiTiet spct join SanPham sp on spct.sanPham.id = sp.id where sp.id = ?1 and spct.trangThai = 1")
    Page<SanPhamChiTiet> findBySanPhamId(Long id, Pageable pageable);
    @Query("select spct from SanPhamChiTiet spct where spct.sanPham.id = ?1")
    Page<SanPhamChiTiet> findSPCTBySanPhamId(Long id, Pageable pageable);
    @Query("select spct from SanPhamChiTiet spct where spct.sanPham.id = ?1")
    List<SanPhamChiTiet> getAllSPCTBySanPhamId(Long id);

    @Query(value = "SELECT spct.id, spct.id_mau_sac, spct.id_kich_thuoc, spct.id_san_pham,spct.so_luong,spct.gia_tien,spct.trang_thai,spct.created_at,spct.updated_at,spct.ma_san_pham,spct.gia_ban_sau_khi_giam FROM san_pham_chi_tiet spct join san_pham sp " +
            "on spct.id_san_pham = sp.id " +
            "where (sp.id_danh_muc = '' or sp.id_danh_muc = :idDanhMuc or :idDanhMuc is null) " +
            "and (sp.id_thuong_hieu = '' or sp.id_thuong_hieu = :idThuongHieu or :idThuongHieu is null) " +
            "and (sp.id_chat_lieu_de = '' or sp.id_chat_lieu_de = :idChatLieuDe or :idChatLieuDe is null) " +
            "and (sp.id_chat_lieu_vai = '' or sp.id_chat_lieu_vai = :idChatLieuVai or :idChatLieuVai is null) " +
            "AND (sp.id = '' OR sp.id = :idSanPham OR :idSanPham IS NULL) " +
            "ORDER BY spct.updated_at DESC", nativeQuery = true)
    Page<SanPhamChiTiet> getAllByFilter(@Param("idDanhMuc") Long idDanhMuc,
                                 @Param("idThuongHieu") Long idThuongHieu,
                                 @Param("idChatLieuVai") Long idChatLieuVai,
                                 @Param("idChatLieuDe") Long idChatLieuDe,
                                 @Param(("idSanPham")) Long idSanPham,
                                 Pageable pageable);
//    Page<SanPhamChiTiet> getAll(Pageable pageable);

    //lấy ra sản phẩm chi tiết có giá cao nhất
    @Query(value = "SELECT spct.gia_tien FROM san_pham_chi_tiet spct WHERE gia_tien = (SELECT MAX(gia_tien) FROM san_pham_chi_tiet) limit 1", nativeQuery = true)
    Double getSanPhamChiTietByGiaTienMax();

    //Lấy ra sản pham theo số lượng
    @Query("select spct from SanPhamChiTiet spct join SanPham  sp on spct.sanPham.id =sp.id where  spct.soLuong <= ?1 and sp.trangThai = 1")
    Page<SanPhamChiTiet> getSanPhamChiTietBySoLuong(int soLuong, Pageable pageable);

    @Query("select count(spct) from SanPhamChiTiet spct join SanPham sp on spct.sanPham.id = sp.id where spct.soLuong = 0 and sp.trangThai = 1")
    int countSanPhamHetHang();
}
