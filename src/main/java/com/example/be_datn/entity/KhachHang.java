package com.example.be_datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "khach_hang")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "ten")
    private String ten;

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

    @Column(name = "gioi_tinh")
    private Boolean gioiTinh;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "trang_thai")
    private Integer trangThai;
    @Column(name = "dia_chi")
    private String diaChi;

    @OneToMany(mappedBy = "khachHang")
    @JsonIgnore
    private List<DiaChi> diaChiList;


}