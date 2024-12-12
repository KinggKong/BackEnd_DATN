package com.example.be_datn.repository;

import com.example.be_datn.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale,Long> {
    boolean existsByTenChienDich(String tenChienDich);

    Page<Sale> findAllBy(Pageable pageable);

    @Query("select s.tenChienDich from Sale s ")
    List<String> getALLTenChienDich();

    @Query(value = "SELECT * \n" +
            "FROM sale s\n" +
            "WHERE (:tenChienDich IS NULL OR :tenChienDich = '' OR s.ten_chien_dich LIKE CONCAT('%', :tenChienDich, '%'))\n" +
            "  AND (:ngayBatDau IS NULL OR s.thoi_gian_bat_dau >= :ngayBatDau)\n" +
            "  AND (:ngayKetThuc IS NULL OR s.thoi_gian_ket_thuc <= :ngayKetThuc)\n" +
            "  AND (:trangThai IS NULL OR s.trang_thai = :trangThai)" +
            "ORDER BY s.updated_at DESC",
            nativeQuery = true)
    Page<Sale> findAllByFilter(String tenChienDich, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, Integer trangThai, Pageable pageable);

    List<Sale> findByThoiGianKetThucBefore(LocalDateTime localDateTime);
    List<Sale> findByThoiGianBatDauBeforeAndTrangThai(LocalDateTime localDateTime, int trangThai);

    @Query("SELECT COUNT(s) > 0 FROM Sale s JOIN s.saleCts saleCt WHERE saleCt.idSanPhamCt = :sanPhamChiTietId AND s.thoiGianKetThuc > :now and s.trangThai = 1")
    boolean existsBySaleCtsIdSanPhamCtAndThoiGianKetThucAfter(@Param("sanPhamChiTietId") Long sanPhamChiTietId, @Param("now") LocalDateTime now);

}
