package com.example.be_datn.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "voucher")
public class Voucher extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_voucher", nullable = false)
    private String tenVoucher;

    @Column(name = "hinh_thuc_giam")
    private String hinhThucGiam;

    @Column(name = "gia_tri_giam")
    private Double giaTriGiam;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "ngay_bat_dau")
    private LocalDateTime ngayBatDau;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "ngay_ket_thuc")
    private LocalDateTime ngayKetThuc;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "ma_voucher")
    private String maVoucher;

    @Column(name = "gia_tri_don_hang_toi_thieu")
    private Double giaTriDonHangToiThieu;

    @Column(name = "gia_tri_giam_toi_da")
    private Double giaTriGiamToiDa;

    @Column(name = "so_luong")
    private Integer soLuong;
}
