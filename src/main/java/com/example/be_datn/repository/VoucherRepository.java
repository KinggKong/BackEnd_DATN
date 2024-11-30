package com.example.be_datn.repository;

import com.example.be_datn.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    // Kiểm tra xem có tồn tại Voucher với tên cụ thể không
    boolean existsVoucherByTenVoucher(String tenVoucher);

    // Tìm kiếm Voucher theo tên với wildcard
    @Query(value = "select v from Voucher v where v.tenVoucher like %:tenVoucher%")
    Page<Voucher> findVoucherByTenVoucherLike(String tenVoucher, Pageable pageable);

    //Lấy ra danh sách voucher hết hạn
    List<Voucher> findByNgayKetThucBefore(LocalDateTime localDateTime);
}
