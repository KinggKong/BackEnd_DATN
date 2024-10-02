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
@Table(name = "san_pham_chi_tiet")
public class SanPhamChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "id_mau")
    private Long idMau;

    @Column(name = "id_size")
    private Long idSize;

    @Column(name = "id_san_pham")
    private Long idSanPham;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "gia_san_pham")
    private Float giaSanPham;

    @Size(max = 255)
    @Column(name = "ma_san_pham")
    private String maSanPham;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

}