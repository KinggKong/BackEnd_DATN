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

@Getter
@Setter
@Entity
@Table(name = "sale_ct")
public class SaleCt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "hinh_thuc_giam")
    private String hinhThucGiam;

    @Column(name = "gia_tri_giam")
    private Float giaTriGiam;

    @Column(name = "id_sale")
    private Long idSale;

    @Column(name = "id_san_pham_ct")
    private Long idSanPhamCt;

    @Column(name = "trang_thai")
    private Integer trangThai;

}