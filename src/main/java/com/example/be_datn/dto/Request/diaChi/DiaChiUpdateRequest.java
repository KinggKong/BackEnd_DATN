package com.example.be_datn.dto.Request.diaChi;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaChiUpdateRequest {
    private Long id_khach_hang;
    @NotBlank(message = "Địa chỉ cụ thể không thể bỏ trống !")
    private String dia_chi_cu_the;
    @NotBlank(message = "Số điện thoại không được để trống")
    @Min(value = 11, message = "Số điện thoại vui lòng nhập đúng")
    private String sdt;
    @NotBlank(message = "Tên tỉnh không được bỏ trống")
    private String tinh;
    @NotBlank(message = "Tên quận không được bỏ trống")
    private String quan;
    @NotBlank(message = "Tên huyện không được bỏ trống")
    private String huyen;
    private String loai_dia_chi;
}
