package com.example.be_datn.repository;

import com.example.be_datn.entity.KhachHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {

    KhachHang findByTen(String ten);

    // Kiểm tra xem có tồn tại khách hàng với số điện thoại cụ thể không
    boolean existsKhachHangBySdt(String sdt);

    // Kiểm tra xem có tồn tại khách hàng với email cụ thể không
    boolean existsKhachHangByEmail(String email);

    // Tìm kiếm khách hàng theo tên với wildcard
    @Query(value = "select k from KhachHang k where k.ten like %:ten%")
    Page<KhachHang> findKhachHangByTenLike(String ten, Pageable pageable);

    // Tìm kiếm khách hàng theo trạng thái
    Page<KhachHang> findByTrangThai(Integer trangThai, Pageable pageable);


    // Tìm kiếm khách hàng theo giới tính (true = Nam, false = Nữ)
    Page<KhachHang> findByGioiTinh(Boolean gioiTinh, Pageable pageable);
}