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

@Getter
@Setter
@Entity
@Table(name = "voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "ten_chien_dich")
    private String tenChienDich;

    @Size(max = 255)
    @Column(name = "hinh_thuc_gian")
    private String hinhThucGian;

    @Column(name = "gia_tri_giam")
    private Float giaTriGiam;

    @Column(name = "ngay_bat_dau")
    private Instant ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private Instant ngayKetThuc;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Size(max = 255)
    @Column(name = "ma_voucher")
    private String maVoucher;

    @Column(name = "gia_tri_don_hang_toi_thieu")
    private Float giaTriDonHangToiThieu;

    @Column(name = "gia_tri_don_hang_toi_da")
    private Float giaTriDonHangToiDa;

    @Column(name = "gia_tri_giam_toi_da")
    private Float giaTriGiamToiDa;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

}