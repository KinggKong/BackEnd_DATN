package com.example.be_datn.repository;

import com.example.be_datn.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Query("SELECT v FROM Voucher v WHERE v.id = :id AND v.trangThai = :trangThai AND v.ngayKetThuc > :now AND v.soLuong > :quantity")
    Optional<Voucher> findByIdAndTrangThaiAndNgayKetThucAfterAndSoLuongGreaterThan(
            @Param("id") Long id,
            @Param("trangThai") int trangThai,
            @Param("now") LocalDateTime now,
            @Param("quantity") int quantity);

    @Query("select v from Voucher v where v.id=:id and v.trangThai=1")
    Optional<Voucher>  check(@Param("id") Long id);




    @Query(value = "select v from Voucher v where v.trangThai = 1 and (:locadate between v.ngayBatDau and v.ngayKetThuc) and v.soLuong >  0 and :tongTien > v.giaTriDonHangToiThieu")
    List<Voucher> getAllVoucherCanUser(@Param("locadate") LocalDateTime localDate, @Param("tongTien") Double tongTien);


    @Query(value = "select v from Voucher v where v.id = :idVoucher and v.trangThai = 1 and :tongTien > v.giaTriDonHangToiThieu and (:locadate between v.ngayBatDau and v.ngayKetThuc)")
    Optional<Voucher> findByIdAndTrangThai(@Param("idVoucher") Long idVoucher,@Param("tongTien") Double tongTien ,@Param("locadate") LocalDateTime localDate);


}
