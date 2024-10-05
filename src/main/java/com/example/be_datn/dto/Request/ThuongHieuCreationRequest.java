package com.example.be_datn.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThuongHieuCreationRequest {
    @NotBlank(message = "Tên thương hiệu không được để trống !")
    String tenThuongHieu;
    int trangThai;
}
