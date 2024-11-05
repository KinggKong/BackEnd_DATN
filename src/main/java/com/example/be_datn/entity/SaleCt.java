package com.example.be_datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "id_sale")
    @JsonIgnore
    private Sale sale;

    @Column(name="tien_giam")
    private Float tienGiam;
    @Column(name = "id_san_pham_ct")
    private Long idSanPhamCt;

    @Column(name = "trang_thai")
    private Integer trangThai;

}