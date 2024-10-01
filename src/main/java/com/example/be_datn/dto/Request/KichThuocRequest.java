package com.example.be_datn.dto.Request;

import com.example.be_datn.entity.KichThuoc;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KichThuocRequest {

    @NotNull(message = "TEN_KICHTHUOC_INVALID")
    @Size(min = 2, message = "TEN_KICHTHUOC_INVALID")
    private String tenKichThuoc;

    @Min(value = 0, message = "TRANGTHAI_KICHTHUOC_INVALID")
    @Max(value = 1, message = "TRANGTHAI_KICHTHUOC_INVALID")
    int trangThai;
}
