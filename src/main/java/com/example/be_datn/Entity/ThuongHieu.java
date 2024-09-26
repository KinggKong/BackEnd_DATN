package com.example.be_datn.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "thuongHieu")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ThuongHieu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Size(min = 3, message = "TEN_THUONGHIEU_INVALID")
    @Column(name = "ten_thuong_hieu")
    String tenThuongHieu;
    @Column(name = "trang_thai")
    Integer trangThai;

}
