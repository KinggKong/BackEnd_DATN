package com.example.be_datn.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class NhanVienResponse {
    private Long id;

    private String ten;

    private String tenDangNhap;

    private String matKhau;

    private String email;

    private String sdt;

    private String avatar;

    private LocalDateTime ngaySinh;

    private String diaChi;

    private Integer vaiTro;

    private Boolean gioiTinh;

    private Instant createdAt;

    private Instant updatedAt;

    private int trangThai;
}
