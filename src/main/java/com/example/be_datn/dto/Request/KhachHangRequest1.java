package com.example.be_datn.dto.Request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhachHangRequest1 {

    @NotNull(message = "TEN_KHACH_HANG_INVALID")
    @Size(max = 255, message = "TEN_KHACH_HANG_INVALID")
    private String ten; // Tên khách hàng

    @NotNull(message = "TEN_KHACH_HANG_INVALID")
    @Size(max = 255, message = "TEN_KHACH_HANG_INVALID")
    private String ma; // Tên khách hàng

    @Email(message = "EMAIL_INVALID")
    @Size(max = 255, message = "EMAIL_INVALID")
    private String email;

    @NotNull(message = "SDT_INVALID")
    @Size(max = 11, message = "SDT_INVALID")
    private String sdt;

    @Size(max = 255, message = "AVATAR_INVALID")
    private String avatar;

    @NotNull(message = "NGAY_SINH_INVALID")
    private LocalDate ngaySinh;


    @NotNull(message = "GIOI_TINH_INVALID")
    private Boolean gioiTinh;

    @Min(value = 0, message = "TRANG_THAI_INVALID")
    @Max(value = 1, message = "TRANG_THAI_INVALID")
    private Integer trangThai;
}