package com.example.be_datn.dto.Response;

import com.example.be_datn.entity.Voucher;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherResponse {

    private Long id;
    private String tenVoucher;
    private String hinhThucGiam;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ngayBatDau;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ngayKetThuc;

    private Integer trangThai;
    private String maVoucher;
    private Float giaTriDonHangToiThieu;
    private Float giaTriGiam;
    private Float giaTriGiamToiDa;
    private Integer soLuong;

    public static VoucherResponse fromVoucher(Voucher voucher) {
        return VoucherResponse.builder()
                .id(voucher.getId())
                .tenVoucher(voucher.getTenVoucher())
                .hinhThucGiam(voucher.getHinhThucGiam())
                .ngayBatDau(voucher.getNgayBatDau())
                .ngayKetThuc(voucher.getNgayKetThuc())
                .trangThai(voucher.getTrangThai())
                .maVoucher(voucher.getMaVoucher())
                .giaTriDonHangToiThieu(voucher.getGiaTriDonHangToiThieu())
                .giaTriGiam(voucher.getGiaTriGiam())
                .giaTriGiamToiDa(voucher.getGiaTriGiamToiDa())
                .soLuong(voucher.getSoLuong())
                .build();
    }
}
