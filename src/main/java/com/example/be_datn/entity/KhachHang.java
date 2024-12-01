package com.example.be_datn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "khach_hang")
public class KhachHang {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "ten")
    private String ten;

//    @Size(max = 255)
//    @Column(name = "ten_dang_nhap")
//    private String tenDangNhap;
//
//    @Size(max = 255)
//    @Column(name = "mat_khau")
//    private String matKhau;

    private String email;

    @Size(max = 11)
    @Column(name = "sdt", length = 11)
    private String sdt;

    @Size(max = 255)
    @Column(name = "avatar")
    private String avatar;

    @Column(name = "ngay_sinh")
    private Instant ngaySinh;

//    @Column(name = "id_dia_chi")
//    private Long idDiaChi;

    @Column(name = "gioi_tinh")
    private Boolean gioiTinh;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "trang_thai")
    private Integer trangThai;


}