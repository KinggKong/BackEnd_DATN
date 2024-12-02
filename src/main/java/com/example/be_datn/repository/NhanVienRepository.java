package com.example.be_datn.repository;

import com.example.be_datn.entity.NhanVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NhanVienRepository extends JpaRepository<NhanVien, Long> {

    // Kiểm tra xem có tồn tại nhân viên với số điện thoại cụ thể không
    boolean existsNhanVienBySdt(String sdt);

    // Kiểm tra xem có tồn tại nhân viên với email cụ thể không
    boolean existsNhanVienByEmail(String email);

    // Tìm kiếm nhân viên theo tên với wildcard
    @Query(value = "select n from NhanVien n where n.ten like %:ten%")
    Page<NhanVien> findNhanVienByTenLike(String ten, Pageable pageable);

    // Tìm kiếm nhân viên theo trạng thái (trạng thái 0 hoặc 1)
    Page<NhanVien> findByTrangThai(Integer trangThai, Pageable pageable);

    NhanVien findByTen(String ten);

    // Tìm kiếm nhân viên theo giới tính (true = Nam, false = Nữ)
    Page<NhanVien> findByGioiTinh(Boolean gioiTinh, Pageable pageable);


}