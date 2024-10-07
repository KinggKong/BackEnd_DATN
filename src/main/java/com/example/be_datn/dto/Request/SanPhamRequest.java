package com.example.be_datn.dto.Request;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SanPhamRequest {
    @Size(min = 8, max = 255, message = "TEN_SANPHAM_INVALID")
    @NotNull(message = "TEN_SANPHAM_INVALID")
    @NotBlank(message = "TEN_SANPHAM_INVALID")
    String tenSanPham;

    String moTa;

    @Min(value = 0,message = "TRANGTHAI_SANPHAM_INVALID")
    @Max(value = 1,message = "TRANGTHAI_SANPHAM_INVALID")
    int trangThai;

    @NotNull(message = "DANHMUC_SANPHAM_INVALID")
    Long idDanhMuc;

    @NotNull(message = "THUONGHIEU_SANPHAM_INVALID")
    Long idThuongHieu;

    @NotNull(message = "LOAIVAI_SANPHAM_INVALID")
    Long idChatLieuVai;

    @NotNull(message = "DEGIAY_SANPHAM_INVALID")
    Long idChatLieuDe;
}
