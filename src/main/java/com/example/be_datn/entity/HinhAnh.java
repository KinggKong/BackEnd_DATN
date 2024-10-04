package com.example.be_datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "hinh_anh")
public class HinhAnh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "src")
    private String src;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "id_san_pham_ct")
    private Long idSanPhamCt;
    @ManyToOne
    @JsonIgnore
    SanPhamChiTiet sanPhamChiTiet;

}