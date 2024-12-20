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
@Builder
public class TaiKhoan extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    @Column(name = "owner_id")
    Long ownerID;

    @Column(name = "ten_dang_nhap")
    String tenDangNhap;

    @Column(name = "forgot_password_token")
    String forgotPasswordToken;

    String email;

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
