package com.example.be_datn.dto.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThongKeSanPhamBanChayResponse {
    private String tenSanPham;
    private Integer tongSoLuongBan;
    private Double tongDoanhThu;
}
