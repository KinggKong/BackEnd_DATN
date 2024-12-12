package com.example.be_datn.dto.Response;

import com.example.be_datn.entity.DiaChi;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiaChiResponse {
    private Long id;
    private Long id_khach_hang;
    private String dia_chi_cu_the;
    private String sdt;
    private String tinh;
    private String quan;
    private String huyen;
    private String loai_dia_chi;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "created_at")
    private LocalDateTime created_at;
    @Column(name = "updated_at")
    private LocalDateTime updated_at;
    private boolean trangThai;


    public static DiaChiResponse toResponse(DiaChi diaChi) {
        if (diaChi == null) {
            return null;
        }
        return DiaChiResponse.builder()
                .id(diaChi.getId())
                .id_khach_hang(diaChi.getKhachHang().getId())
                .dia_chi_cu_the(diaChi.getDia_chi_cu_the())
                .sdt(diaChi.getSdt())
                .tinh(diaChi.getTinh())
                .quan(diaChi.getQuan())
                .huyen(diaChi.getHuyen())
                .loai_dia_chi(diaChi.getLoai_dia_chi())
                .created_at(diaChi.getCreated_at())
                .updated_at(diaChi.getUpdated_at())
                .trangThai(diaChi.isTrang_thai())
                .build();
    }

}
