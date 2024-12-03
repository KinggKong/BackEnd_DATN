package com.example.be_datn.dto.Request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    String newPasswrod;
    String token;
}
