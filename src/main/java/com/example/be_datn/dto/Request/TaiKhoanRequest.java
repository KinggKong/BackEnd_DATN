package com.example.be_datn.dto.Request;


import com.example.be_datn.entity.VaiTro;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaiKhoanRequest {

    @Size(min = 8, max = 255, message = "TEN_TAIKHOAN_INVALID")
    @NotNull(message = "TEN_TAIKHOAN_INVALID")
    @NotBlank(message = "TEN_TAIKHOAN_INVALID")
    String tenDangNhap;

    @Size(min = 8, max = 255, message = "MATKHAU_TAIKHOAN_INVALID")
    @NotNull(message = "MATKHAU_TAIKHOAN_INVALID")
    @NotBlank(message = "MATKHAU_TAIKHOAN_INVALID")
    String matKhau;

    VaiTro vaiTro;

    @Min(value = 0, message = "TRANGTHAI_TAIKHOAN_INVALID")
    @Max(value = 1, message = "TRANGTHAI_TAIKHOAN_INVALID")
    int trangThai;
}
