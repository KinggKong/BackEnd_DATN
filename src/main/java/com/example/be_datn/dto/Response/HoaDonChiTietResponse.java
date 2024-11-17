package com.example.be_datn.dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class HoaDonChiTietResponse {
    private Long id;

    SanPhamChiTietResponse sanPhamChiTietResponse;

    private Integer soLuong;

    private double giaTien;

}
