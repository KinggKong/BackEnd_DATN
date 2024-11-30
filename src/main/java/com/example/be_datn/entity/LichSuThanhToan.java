package com.example.be_datn.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "lich_su_thanh_toan")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LichSuThanhToan extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_hoa_don")
    HoaDon hoaDon;

    @Column(name = "so_tien")
    double soTien;


    @Column(name = "payment_method")
    String paymentMethod;


    String type;

    String status;
}
