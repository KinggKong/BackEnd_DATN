package com.example.be_datn.dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class InfoOrder {
    HoaDonResponse hoaDonResponse;
    List<HoaDonChiTietResponse> hoaDonChiTietResponse;
}
