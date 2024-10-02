package com.example.be_datn.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kich_thuoc")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KichThuoc extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "ten_kich_thuoc")
    String tenKichThuoc;

    @Column(name = "trang_thai")
    int trangThai;

}
