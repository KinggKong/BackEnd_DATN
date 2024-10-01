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
@Table(name = "san_pham_chi_tiet")
public class SanPhamChiTiet extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mau_sac")
    MauSac mauSac;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kich_thuoc")
    KichThuoc kichThuoc;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_san_pham")
    SanPham sanPham;
    @Column(name = "ma_san_pham")
    String maSanPham;
    @Column(name = "so_luong")
    int soLuong;
    @Column(name = "gia_tien")
    Double giaBan;
    @Column(name = "trang_thai")
    int trangThai;
}

