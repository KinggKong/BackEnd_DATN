package com.example.be_datn.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class NhanVienUpdate {
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
}