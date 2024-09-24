package com.example.be_datn.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MauSac")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MauSac extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Size(min=3, message = "TEN_MAUSAC_INVALID")
    @Column(name = "ten_mau")
    String tenMau;

    @Column(name = "trang_thai")
    int trangThai;

}
