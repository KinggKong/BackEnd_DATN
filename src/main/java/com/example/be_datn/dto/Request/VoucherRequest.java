package com.example.be_datn.dto.Request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherRequest {

    @NotNull(message = "TEN_VOUCHER_INVALID") // Thay đổi mã thông báo
    @Size(max = 255, message = "TEN_VOUCHER_INVALID") // Thay đổi mã thông báo
    private String tenVoucher; // Thay đổi tên thuộc tính

    @Size(max = 255, message = "HINH_THUC_GIAM_INVALID") // Sửa lỗi chính tả
    private String hinhThucGiam; // Thay đổi tên thuộc tính

    @NotNull(message = "GIA_TRI_GIAM_INVALID")
    @PositiveOrZero(message = "GIA_TRI_GIAM_INVALID")
    private Float giaTriGiam;

    @NotNull(message = "NGAY_BAT_DAU_INVALID")
    private LocalDateTime ngayBatDau; // Thay đổi từ Instant sang LocalDateTime

    @NotNull(message = "NGAY_KET_THUC_INVALID")
    private LocalDateTime ngayKetThuc; // Thay đổi từ Instant sang LocalDateTime

    @Min(value = 0, message = "TRANGTHAI_VOUCHER_INVALID")
    @Max(value = 1, message = "TRANGTHAI_VOUCHER_INVALID")
    private Integer trangThai;

    @NotNull(message = "MA_VOUCHER_INVALID")
    @Size(max = 255, message = "MA_VOUCHER_INVALID")
    private String maVoucher;

    @NotNull(message = "GIA_TRI_DON_HANG_TOI_THIEU_INVALID")
    @PositiveOrZero(message = "GIA_TRI_DON_HANG_TOI_THIEU_INVALID")
    private Float giaTriDonHangToiThieu;

    @PositiveOrZero(message = "GIA_TRI_GIAM_TOI_DA_INVALID")
    private Float giaTriGiamToiDa;

    @Min(value = 0, message = "SO_LUONG_INVALID")
    private Integer soLuong;
}
