package com.example.be_datn.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "tai_khoan")
public class TaiKhoan extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    @Column(name = "ten_dang_nhap")
    String tenDangNhap;

    @Column(name = "ma")
    String ma;

    @Column(name = "mat_khau")
    String matKhau;

    @ManyToOne
    @JoinColumn(name = "id_vai_tro")
    VaiTro vaiTro;

    @Column(name = "trang_thai")
    int trangThai;

}
