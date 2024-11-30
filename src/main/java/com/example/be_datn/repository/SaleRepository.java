package com.example.be_datn.repository;

import com.example.be_datn.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale,Long> {
    boolean existsByTenChienDich(String tenChienDich);
    Page<Sale> findAllBy(Pageable pageable);
    @Query("select s.tenChienDich from Sale s")
    List<String> getALLTenChienDich();
    @Query(value = "SELECT * \n" +
            "FROM sale s\n" +
            "WHERE (:tenChienDich IS NULL OR :tenChienDich = '' OR s.ten_Chien_Dich LIKE CONCAT('%', :tenChienDich, '%'))\n" +
            "  AND (:ngayBatDau IS NULL OR s.thoi_Gian_Bat_Dau >= :ngayBatDau)\n" +
            "  AND (:ngayKetThuc IS NULL OR s.thoi_Gian_Ket_Thuc <= :ngayKetThuc)\n" +
            "  AND (:trangThai IS NULL OR :trangThai = '' OR s.trang_Thai = :trangThai)",
            nativeQuery = true)
    Page<Sale> findAllByFilter(String tenChienDich, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc,  Integer trangThai, Pageable pageable);
}
