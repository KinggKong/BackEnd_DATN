package com.example.be_datn.dto.Response;

import com.example.be_datn.entity.HoaDon;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class LichSuThanhToanResponse {
    private Long id;

    String  maHoaDon;

    double soTien;

    String paymentMethod;

    String type;

    String status;

    LocalDateTime createdAt;
}
