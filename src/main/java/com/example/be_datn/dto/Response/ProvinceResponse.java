package com.example.be_datn.dto.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProvinceResponse {
    String code;
    String fullName;
    double latitude;
    double longitude;
}
