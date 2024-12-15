package com.example.be_datn.repository;

import com.example.be_datn.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    // Kiểm tra xem có tồn tại Voucher với tên cụ thể không
    boolean existsVoucherByTenVoucher(String tenVoucher);

    // Tìm kiếm Voucher theo tên với wildcard
    @Query(value = "select v from Voucher v where v.tenVoucher like %:tenVoucher%")
    Page<Voucher> findVoucherByTenVoucherLike(String tenVoucher, Pageable pageable);

    @Query(value = "SELECT * \n" +
            "FROM voucher v\n" +
            "WHERE (:tenChienDich IS NULL OR :tenChienDich = '' OR v.ten_voucher LIKE CONCAT('%', :tenChienDich, '%'))\n" +
            "  AND (:ngayBatDau IS NULL OR v.ngay_bat_dau >= :ngayBatDau)\n" +
            "  AND (:ngayKetThuc IS NULL OR v.ngay_ket_thuc <= :ngayKetThuc)\n" +
            "  AND (:trangThai IS NULL  OR v.trang_thai = :trangThai) " +
            "ORDER BY v.updated_at DESC",
            nativeQuery = true)
    Page<Voucher> findAllByFilter(String tenChienDich, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, Integer trangThai, Pageable pageable);

    //Lấy ra danh sách voucher hết hạn
    List<Voucher> findByNgayKetThucBefore(LocalDateTime localDateTime);

    @Query("SELECT v FROM Voucher v WHERE v.trangThai = 1 AND :tongTien >= v.giaTriDonHangToiThieu")
    List<Voucher> findAvailableVouchers(@Param("tongTien") double tongTien);

    Voucher findByMaVoucher(String ma);


}
