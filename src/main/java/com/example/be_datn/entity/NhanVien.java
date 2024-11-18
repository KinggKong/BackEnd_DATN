package com.example.be_datn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
    private String ten;
    private String tenDangNhap;
    private String matKhau;
    private String email;
    private String sdt;
    private String avatar;
    private LocalDateTime ngaySinh;
    private String diaChi;
    private Integer vaiTro;
    private Boolean gioiTinh;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer trangThai;

}