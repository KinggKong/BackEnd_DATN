package com.example.be_datn.dto.Request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SanPhamChiTietRequest {

    @NotNull(message = "ID_MAUSAC_INVALID")
    Long id_mauSac;
    @NotNull(message = "ID_KICHTHUOC_INVALID")
    Long id_kichThuoc;
    @NotNull(message = "ID_SAN_PHAM_INVALID")
    Long id_sanPham;
    @NotBlank(message = "MA_SAN_PHAM_INVALID")
    String maSanPham;
    @NotNull(message = "SO_LUONG_INVALID")
    @Positive(message = "SO_LUONG_INVALID")
    int soLuong;
    @NotNull(message = "GIA_BAN_INVALID")
    @Positive(message = "GIA_BAN_INVALID")
    Double giaBan;
    @NotNull(message = "TRANG_THAI_INVALID")
    int trangThai;
    List<String> hinhAnh;
}
