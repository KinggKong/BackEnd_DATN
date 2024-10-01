package com.example.be_datn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "hoa_don_ban_ct")
public class HoaDonBanCt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "id_san_pham_ct")
    private Long idSanPhamCt;

    @Column(name = "id_hoa_don")
    private Long idHoaDon;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "gia_tien")
    private Float giaTien;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "trang_thai")
    private Integer trangThai;

}