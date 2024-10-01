package com.example.be_datn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "nhan_vien")
public class NhanVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "ten")
    private String ten;

    @Size(max = 255)
    @Column(name = "ten_dang_nhap")
    private String tenDangNhap;

    @Size(max = 255)
    @Column(name = "mat_khau")
    private String matKhau;

    @Size(max = 255)
    @Column(name = "email")
    private String email;

    @Size(max = 11)
    @Column(name = "sdt", length = 11)
    private String sdt;

    @Size(max = 255)
    @Column(name = "avatar")
    private String avatar;

    @Column(name = "ngay_sinh")
    private LocalDateTime ngaySinh;

    @Size(max = 255)
    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "vai_tro")
    private Integer vaiTro;

    @Column(name = "gioi_tinh")
    private Boolean gioiTinh;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "trang_thai")
    private int trangThai;

}