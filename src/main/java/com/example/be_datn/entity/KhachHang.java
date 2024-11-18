package com.example.be_datn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "khach_hang")
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ten;
    private String tenDangNhap;
    private String matKhau;
    private String sdt;
    private String avatar;
    private LocalDateTime ngaySinh;
    private Long idDiaChi;
    private Boolean gioiTinh;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer trangThai;
    private String email;

}