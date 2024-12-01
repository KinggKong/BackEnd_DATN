
package com.example.be_datn.dto.Request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhanVienRequest {

    @NotNull(message = "TEN_NHAN_VIEN_INVALID")
    @Size(max = 255, message = "TEN_NHAN_VIEN_INVALID")
    private String ten; // Tên nhân viên


    @Email(message = "EMAIL_INVALID")
    @Size(max = 255, message = "EMAIL_INVALID")
    private String email; // Email nhân viên

    @NotNull(message = "SDT_INVALID")
    @Size(max = 11, message = "SDT_INVALID")
    private String sdt; // Số điện thoại nhân viên

    @Size(max = 255, message = "AVATAR_INVALID")
    private String avatar; // Avatar nhân viên

    @NotNull(message = "NGAY_SINH_INVALID")
    private LocalDate ngaySinh; // Ngày sinh nhân viên

    @NotNull(message = "DIA_CHI_INVALID")
    @Size(max = 255, message = "DIA_CHI_INVALID")
    private String diaChi; // Địa chỉ nhân viên


    @NotNull(message = "GIOI_TINH_INVALID")
    private Boolean gioiTinh; // Giới tính nhân viên

    @Min(value = 0, message = "TRANG_THAI_INVALID")
    @Max(value = 1, message = "TRANG_THAI_INVALID")
    private Integer trangThai; // Trạng thái nhân viên (ví dụ: 0 - inactive, 1 - active)
}
