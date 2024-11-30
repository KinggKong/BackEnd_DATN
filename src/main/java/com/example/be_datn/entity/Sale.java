package com.example.be_datn.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "sale")
public class Sale extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "ten_chien_dich")
    private String tenChienDich;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "thoi_gian_bat_dau")
    private LocalDateTime thoiGianBatDau;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "thoi_gian_ket_thuc")
    private LocalDateTime thoiGianKetThuc;
    @Size(max = 255)
    @Column(name = "hinh_thuc_giam")
    private String hinhThucGiam;

    @Column(name = "gia_tri_giam")
    private Float giaTriGiam;
    @Column(name = "trang_thai")
    private Integer trangThai;

    @OneToMany(mappedBy = "sale", fetch = FetchType.LAZY)
    private List<SaleCt> saleCts;

}