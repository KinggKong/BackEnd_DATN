package com.example.be_datn.dto.Response;

import com.example.be_datn.entity.VaiTro;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaiKhoanResponse {
    Long id;
    String tenDangNhap;
    String ma;
    VaiTro vaiTro;
    int trangThai;
}
