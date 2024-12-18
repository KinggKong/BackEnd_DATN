package com.example.be_datn.repository;

import com.example.be_datn.entity.KhachHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

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

    // Tìm kiếm khách hàng theo ID địa chỉ
//    Page<KhachHang1> findByIdDiaChi(Long idDiaChi, Pageable pageable);
//
//    // Tìm kiếm khách hàng theo ID tài khoản
//    Page<KhachHang1> findByIdTaiKhoan(Long idTaiKhoan, Pageable pageable);


    // Tìm kiếm khách hàng theo giới tính (true = Nam, false = Nữ)
    Page<KhachHang> findByGioiTinh(Boolean gioiTinh, Pageable pageable);

    @Query(value = "select kh from KhachHang  kh inner  join TaiKhoan  tk on tk.ownerID = kh.id where (kh.email = :username or tk.tenDangNhap = :username) and kh.trangThai = 1")
    Optional<KhachHang> checkIsActive(@Param("username") String username);
}