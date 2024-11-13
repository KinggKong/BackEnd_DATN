package com.example.be_datn.dto.Response;

import com.example.be_datn.entity.Sale;
import com.example.be_datn.entity.SaleCt;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleResponse {
    Long id;
    String tenChienDich;
    String hinhThucGiam;
    Float giaTriGiam;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime thoiGianBatDau;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime thoiGianKetThuc;
    int trangThai;
    List<SaleCt> saleCts;
    public static SaleResponse fromSale(Sale sale) {
        return SaleResponse.builder()
                .id(sale.getId())
                .tenChienDich(sale.getTenChienDich())
                .hinhThucGiam(sale.getHinhThucGiam())
                .giaTriGiam(sale.getGiaTriGiam())
                .thoiGianBatDau(sale.getThoiGianBatDau())
                .thoiGianKetThuc(sale.getThoiGianKetThuc())
                .trangThai(sale.getTrangThai())
                .saleCts(sale.getSaleCts())
                .build();
    }
}
