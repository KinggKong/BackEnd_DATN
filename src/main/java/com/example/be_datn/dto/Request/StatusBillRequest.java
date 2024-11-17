package com.example.be_datn.dto.Request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusBillRequest {
    Long idNhanvien;
    Long idHoaDon;
    String status;
}

