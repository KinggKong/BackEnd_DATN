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

@Getter
@Setter
@Entity
@Table(name = "san_pham")
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "ten_san_pham")
    private String tenSanPham;

    @Size(max = 255)
    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "id_danh_muc")
    private Long idDanhMuc;

    @Column(name = "id_thuong_hieu")
    private Long idThuongHieu;

    @Column(name = "id_chat_lieu_vai")
    private Long idChatLieuVai;

    @Column(name = "id_chat_lieu")
    private Long idChatLieu;

}