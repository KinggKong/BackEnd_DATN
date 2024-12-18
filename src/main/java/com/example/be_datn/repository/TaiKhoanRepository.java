package com.example.be_datn.repository;

import com.example.be_datn.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Long> {
    boolean existsTaiKhoanByMa(String ma);

    boolean existsByEmail(String email);

    Optional<TaiKhoan> findByEmail(String email);

    boolean existsByTenDangNhap(String tenDangNhap);

    @Query(value = "select  tk from TaiKhoan  tk where tk.email = :keyword or tk.tenDangNhap = :keyword")
    Optional<TaiKhoan> findByEmailAndUsername(@Param("keyword") String keyword);

    Optional<TaiKhoan> findByTenDangNhap(String tenDangNhap);
    @Query("select tk from TaiKhoan tk where tk.email = :email")
    Optional<TaiKhoan> findByOwnerID(String email);
}
