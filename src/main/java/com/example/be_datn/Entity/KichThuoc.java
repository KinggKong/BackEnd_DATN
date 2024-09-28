package com.example.be_datn.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "KichThuoc")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class KichThuoc extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @jakarta.validation.constraints.Size(min = 2, message = "TEN_KICHTHUOC_INVALID")
    @Column(name = "ten_kich_thuoc")
    private String tenKichThuoc;

    @Column(name = "trang_thai")
    Integer trangThai;


}


