package com.example.be_datn.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "thoi_gian_giam_gia")
    LocalDateTime thoiGianGiamGia;

    @Column(name = "trang_thai")
    private Integer trangThai;

}