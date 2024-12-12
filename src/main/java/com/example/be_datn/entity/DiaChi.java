package com.example.be_datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dia_chi")
public class DiaChi extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_khach_hang", referencedColumnName = "id")
    KhachHang khachHang;
    private String dia_chi_cu_the;
    private String sdt;
    private String tinh;
    private String quan;
    private String huyen;
    private String loai_dia_chi;
    private boolean trang_thai;

}