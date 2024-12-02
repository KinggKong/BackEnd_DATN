package com.example.be_datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "hoa_don")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HoaDon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "ma_hoa_don", nullable = false)
    private String maHoaDon;

    @Size(max = 255)
    @NotNull
    @Column(name = "ten_nguoi_nhan", nullable = false)
    private String tenNguoiNhan;

    @Size(max = 255)
    @NotNull
    @Column(name = "dia_chi_nhan", nullable = false)
    private String diaChiNhan;

    @Size(max = 10)
    @NotNull
    @Column(name = "sdt", nullable = false, length = 10)
    private String sdt;

    @NotNull
    @Column(name = "tong_tien", nullable = false)
    private Double tongTien;

    @NotNull
    @Column(name = "tien_sau_giam", nullable = false)
    private Double tienSauGiam;

    @NotNull
    @Column(name = "tien_ship", nullable = false)
    private Double tienShip;

    @Lob
    @Column(name = "ghi_chu")
    private String ghiChu;

    @Size(max = 255)
    @NotNull
    @Column(name = "loai_hoa_don", nullable = false)
    private String loaiHoaDon;

    @Size(max = 255)
    @Column(name = "email")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idNhanVien", referencedColumnName = "id")
    private NhanVien nhanVien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idKhachHang", referencedColumnName = "id")
    private KhachHang khachHang;

    @Size(max = 255)
    @NotNull
    @Column(name = "hinh_thuc_thanh_toan", nullable = false)
    private String hinhThucThanhToan;

    @NotNull
    @Column(name = "trang_thai", nullable = false)
    private String trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_voucher", referencedColumnName = "id")
    private Voucher voucher;

    @Column(name = "so_tien_giam")
    private Float soTienGiam;

    @JsonIgnore
    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HoaDonCT> chiTietList;

}