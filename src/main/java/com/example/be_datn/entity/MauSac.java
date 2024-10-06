package com.example.be_datn.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mau_sac")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MauSac extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "ten_mau")
    String tenMau;

    @Column(name = "trang_thai")
    int trangThai;

}
