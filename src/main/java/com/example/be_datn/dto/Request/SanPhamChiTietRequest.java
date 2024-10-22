package com.example.be_datn.dto.Request;

import com.example.be_datn.entity.KichThuoc;
import com.example.be_datn.entity.MauSac;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
