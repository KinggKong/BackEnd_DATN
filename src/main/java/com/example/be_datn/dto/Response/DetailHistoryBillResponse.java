package com.example.be_datn.dto.Response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailHistoryBillResponse {
    HoaDonResponse hoaDonResponse;
    List<LichSuHoaDonResponse> lichSuHoaDonResponses;
    List<HoaDonChiTietResponse> hoaDonChiTietResponses;
    LichSuThanhToanResponse lichSuThanhToanResponse;
}
