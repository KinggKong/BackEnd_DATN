package com.example.be_datn.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gio_hang")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GioHang extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "id_khach_hang")
    KhachHang khachHang;

    String ma;

    @Column(name = "trang_thai")
    int trangThai;
}
