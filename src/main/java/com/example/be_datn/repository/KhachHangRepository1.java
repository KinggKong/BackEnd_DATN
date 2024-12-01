package com.example.be_datn.repository;

import com.example.be_datn.entity.KhachHang1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KhachHangRepository1 extends JpaRepository<KhachHang1, Long> {

    // Kiểm tra xem có tồn tại khách hàng với số điện thoại cụ thể không
    boolean existsKhachHangBySdt(String sdt);

    // Kiểm tra xem có tồn tại khách hàng với email cụ thể không
    boolean existsKhachHangByEmail(String email);

    // Tìm kiếm khách hàng theo tên với wildcard
    @Query(value = "select k from KhachHang1 k where k.ten like %:ten%")
    Page<KhachHang1> findKhachHangByTenLike(String ten, Pageable pageable);

    // Tìm kiếm khách hàng theo trạng thái
    Page<KhachHang1> findByTrangThai(Integer trangThai, Pageable pageable);

    // Tìm kiếm khách hàng theo ID địa chỉ
//    Page<KhachHang1> findByIdDiaChi(Long idDiaChi, Pageable pageable);
//
//    // Tìm kiếm khách hàng theo ID tài khoản
//    Page<KhachHang1> findByIdTaiKhoan(Long idTaiKhoan, Pageable pageable);

    // Tìm kiếm khách hàng theo giới tính (true = Nam, false = Nữ)
    Page<KhachHang1> findByGioiTinh(Boolean gioiTinh, Pageable pageable);
}
