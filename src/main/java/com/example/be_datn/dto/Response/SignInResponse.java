package com.example.be_datn.dto.Response;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
public class SignInResponse {
    private Long id;
    private String type = "Bearer";
    private String accessToken;
    private String username;
    private String email;
    private Boolean isActive;
    private String avatar;
    private String roleName;
    private Long idGioHang;
}
