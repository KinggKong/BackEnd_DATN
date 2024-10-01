package com.example.be_datn.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "san_pham")
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "ten_san_pham")
    String tenSanPham;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_danh_muc")
    DanhMuc danhMuc;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chat_lieu_de")
    ChatLieuDe chatLieuDe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_thuong_hieu")
    ThuongHieu thuongHieu;
    @Column(name = "mo_ta")
    String moTa;

    @Column(name = "trang_thai")
    int trangThai;
}
