package com.example.be_datn.dto.Request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleRequest {
    @NotNull(message = "Ten chien dich khong duoc de trong")
    String tenChienDich;
    @NotNull(message = "Thoi gian bat dau khong duoc de trong")
    LocalDateTime thoiGianBatDau;
    @NotNull(message = "Thoi gian ket thuc khong duoc de trong")
    LocalDateTime thoiGianKetThuc;
    @NotNull(message = "Hinh thuc giam khong duoc de trong")
    String hinhThucGiam;
    @NotNull(message = "Gia tri giam khong duoc de trong")
    @PositiveOrZero(message = "Gia tri giam phai lon hon hoac bang 0")
    @Max(value = 100, message = "Gia tri giam phai nho hon hoac bang 100")
    Float giaTriGiam;
    @Min(value = 0, message = "Trang thai khong hop le")
    @Max(value = 1, message = "Trang thai khong hop le")
    int trangThai;
    @NotNull(message = "Danh sach san pham chi tiet khong duoc de trong")
    List<Long> idSanPhamChiTiet;
}
