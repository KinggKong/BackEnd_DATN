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
    DiaChiResponse diaChi;


    public static KhachHangResponse toResponse(KhachHang khachHang) {
        if (khachHang == null) {
            return null;
        }

        DiaChiResponse diaChiResponse = null;
        if (khachHang.getDiaChiList() != null) {
            Optional<DiaChiResponse> diaChiOptional = khachHang.getDiaChiList().stream()
                    .filter(diaChi -> diaChi.isTrang_thai() == true)
                    .map(DiaChiResponse::toResponse)
                    .findFirst();
            diaChiResponse = diaChiOptional.orElse(
                    khachHang.getDiaChiList().stream()
                            .map(DiaChiResponse::toResponse)
                            .findFirst()
                            .orElse(null)
            );
        }

        return KhachHangResponse.builder()
                .id(khachHang.getId())
                .ten(khachHang.getTen())
                .email(khachHang.getEmail())
                .sdt(khachHang.getSdt())
                .avatar(khachHang.getAvatar())
                .ma(khachHang.getMa())
                .ngaySinh(khachHang.getNgaySinh())
                .gioiTinh(khachHang.getGioiTinh())
                .trangThai(khachHang.getTrangThai())
                .diaChiStr(khachHang.getDiaChi())
                .diaChi(diaChiResponse)
                .build();
    }

}
