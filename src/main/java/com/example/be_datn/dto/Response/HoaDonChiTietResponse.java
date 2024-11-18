package com.example.be_datn.dto.Response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
public class HoaDonChiTietResponse {
    Long id;
    Long idGioHang;
    // add  a field at 18/11
    Long idSanPhamChiTiet;
    String tenSanPhamChiTiet;
    Integer soLuong;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Double tongTien;
    Integer trangThai;
}
