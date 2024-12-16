package com.example.be_datn.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "vai_tro")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaiTro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "ten_vai_tro")
    private String tenVaiTro;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "trang_thai")
    private Integer trangThai;

}