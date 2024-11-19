package com.example.be_datn.dto.Response;

import com.example.be_datn.entity.SaleCt;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleCTResponse {
 private Long id;
    private Long idSanPhamCt;
    private Long idSale;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime thoiGianBatDau;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime thoiGianKetThuc;
    private int trangThai;
    private int trangThaiSale;
    private Float phanTramGiam;
    private Float giaGiam;
    public static SaleCTResponse fromSaleCT(SaleCt saleCT) {
        return SaleCTResponse.builder()
                .id(saleCT.getId())
                .giaGiam(saleCT.getTienGiam())
                .idSale(saleCT.getSale().getId())
                .phanTramGiam(saleCT.getGiaTriGiam())
                .thoiGianBatDau(saleCT.getSale().getThoiGianBatDau())
                .thoiGianKetThuc(saleCT.getSale().getThoiGianKetThuc())
                .trangThai(saleCT.getTrangThai())
                .trangThaiSale(saleCT.getSale().getTrangThai())
                .idSanPhamCt(saleCT.getIdSanPhamCt())
                .build();
    }
}
