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
@Table(name = "sale")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "ten_chien_dich")
    private String tenChienDich;

    @Column(name = "thoi_gian_bat_dau")
    private Instant thoiGianBatDau;

    @Column(name = "thoi_gian_ket_thuc")
    private Instant thoiGianKetThuc;

    @Column(name = "trang_thai")
    private Integer trangThai;

}