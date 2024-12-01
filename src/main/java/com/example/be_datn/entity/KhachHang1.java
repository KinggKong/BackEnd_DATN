package com.example.be_datn.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "khach_hang")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KhachHang1 extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "ten")
    private String ten;

    @Size(max = 255)
    @Column(name = "ma")
    private String ma;

    private String email;

    @Size(max = 11)
    @Column(name = "sdt", length = 11)
    private String sdt;

    @Size(max = 255)
    @Column(name = "avatar")
    private String avatar;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

//    @Column(name = "id_dia_chi")
//    private Long idDiaChi;

//    @Column(name = "id_tai_khoan")
//    private Long idTaiKhoan;

    @Column(name = "gioi_tinh")
    private Boolean gioiTinh;


    @Column(name = "trang_thai")
    private Integer trangThai;
}
