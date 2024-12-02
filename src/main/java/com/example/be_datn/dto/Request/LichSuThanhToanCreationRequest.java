package com.example.be_datn.dto.Request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LichSuThanhToanCreationRequest {
    String maGiaoDich;
    Double soTien;
    String phuongThucThanhToan;
    Long hoaDonId;
}
