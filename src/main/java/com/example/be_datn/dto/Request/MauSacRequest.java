package com.example.be_datn.dto.Request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MauSacRequest {
    @NotNull(message = "TEN_MAUSAC_INVALID")
    @Size(min = 2, message = "TEN_MAUSAC_INVALID")
    String tenMau;
    @Min(value = 0,message = "TRANGTHAI_MAUSAC_INVALID")
    @Max(value = 1,message = "TRANGTHAI_MAUSAC_INVALID")
    int trangThai;
}
