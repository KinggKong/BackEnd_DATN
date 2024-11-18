package com.example.be_datn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

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
    Long id;
    String maHoaDon;
    String tenNguoiNhan;
    String diaChiNhan;
    String sdt;
    Double tongTien;
    Double tienSauGiam;
    Double tienShip;
    String ghiChu;
    Integer soLuong;
    String loaiHoaDon;
    String email;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idNhanVien", referencedColumnName = "id")
    private NhanVien nhanVien;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idKhachHang", referencedColumnName = "id")
    private KhachHang khachHang;
    String hinhThucThanhToan;
    Integer trangThai;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_voucher", referencedColumnName = "id")
    private Voucher voucher;
    Double soTienGiam;

}