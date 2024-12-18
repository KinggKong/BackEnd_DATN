package com.example.be_datn.dto.Response;


import com.example.be_datn.entity.DiaChi;
import com.example.be_datn.entity.KhachHang;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KhachHangResponse {
    Long id;
    String ten;
    String email;
    String sdt;
    String avatar;
    String ma;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate ngaySinh;
    Boolean gioiTinh;
    Integer trangThai;
    String diaChiStr;

}
