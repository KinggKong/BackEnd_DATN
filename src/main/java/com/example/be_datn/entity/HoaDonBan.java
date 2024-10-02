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
@Table(name = "hoa_don_ban")
public class HoaDonBan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "ngay_tao")
    private Instant ngayTao;

    @Size(max = 255)
    @Column(name = "ma_hoa_don")
    private String maHoaDon;

    @Size(max = 255)
    @Column(name = "ten_nguoi_nhan")
    private String tenNguoiNhan;

    @Size(max = 255)
    @Column(name = "dia_chi_nhan")
    private String diaChiNhan;

    @Size(max = 11)
    @Column(name = "sdt", length = 11)
    private String sdt;

    @Column(name = "tong_tien")
    private Float tongTien;

    @Size(max = 255)
    @Column(name = "ma_giam_gia")
    private String maGiamGia;

    @Size(max = 255)
    @Column(name = "ghi_chu")
    private String ghiChu;

    @Size(max = 255)
    @Column(name = "email")
    private String email;

    @Column(name = "id_nhan_vien")
    private Long idNhanVien;

    @Column(name = "id_khach_hang")
    private Long idKhachHang;

    @Size(max = 255)
    @Column(name = "hinh_thuc_thanh_toan")
    private String hinhThucThanhToan;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "id_voucher")
    private Long idVoucher;

    @Column(name = "id_hoa_don")
    private Long idHoaDon;

    @Column(name = "so_tien_giam")
    private Float soTienGiam;

}