package com.example.be_datn.entity;



import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;


@Getter
@Setter


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@Table(name = "san_pham")
public class SanPham extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;


    @Column(name = "ten_san_pham", nullable = false)
    String tenSanPham;

    @Lob
    @Column(name = "mo_ta")
    String moTa;


    @ColumnDefault("1")
    @Column(name = "trang_thai", nullable = false)
    int trangThai;


    @ManyToOne
    @JoinColumn(name = "id_danh_muc")
    DanhMuc danhMuc;


    @ManyToOne
    @JoinColumn(name = "id_thuong_hieu")
    ThuongHieu thuongHieu;


    @ManyToOne
    @JoinColumn(name = "id_chat_lieu_vai")
    ChatLieuVai chatLieuVai;


    @ManyToOne
    @JoinColumn(name = "id_chat_lieu_de")
    ChatLieuDe chatLieuDe;
    @OneToMany(mappedBy = "sanPham", fetch = FetchType.LAZY)
    List<SanPhamChiTiet> sanPhamChiTietList;
}

