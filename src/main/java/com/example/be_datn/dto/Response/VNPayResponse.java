package com.example.be_datn.dto.Response;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VNPayResponse {
    public String code;
    public String message;
    public String paymentUrl;
}
