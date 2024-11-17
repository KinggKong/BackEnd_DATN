package com.example.be_datn.dto.Response;

import com.example.be_datn.entity.GioHangChiTiet;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AboutProductShopOn {
    Long idGioHang;
    List<GioHangChiTietResponse> gioHangChiTietList;
    double totalPrice;
}
