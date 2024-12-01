package com.example.be_datn.dto.Request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {
    String username;
    String name;
    String email;
    String code;
    String password;
}
