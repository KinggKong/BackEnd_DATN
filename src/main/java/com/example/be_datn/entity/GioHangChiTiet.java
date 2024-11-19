package com.example.be_datn.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "gio_hang_ct")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GioHangChiTiet extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_gio_hang")
    GioHang gioHang;


    @ManyToOne
    @JoinColumn(name = "id_san_pham_chi_tiet")
    SanPhamChiTiet sanPhamChiTiet;

    @Column(name = "so_luong")
    int soLuong;
    @Column(name = "gia_tien")
    Double giaTien;

    @Column(name = "trang_thai")
    private Integer trangThai;

}