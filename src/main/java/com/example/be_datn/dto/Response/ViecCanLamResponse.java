package com.example.be_datn.dto.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViecCanLamResponse {
    private int donChoXacNhan;
    private int donDangGiaoHang;
    private int donChoLayHang;
    private int sanPhamHetHang;

}
