package com.example.be_datn.dto.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThongKeResponse {
    private int donThanhCong;
    private int donHuy;
    private int traHang;
    private int choXacNhan;
    private int dangGiaoHang;
    private int choLayHang;
    private int choThanhToan;
    private double tongDoanhThu;
    private int tongSanPhamBan;
}

