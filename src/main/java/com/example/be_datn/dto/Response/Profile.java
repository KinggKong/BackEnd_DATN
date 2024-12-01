package com.example.be_datn.dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Profile {
    private Long id;
    private String ten;
    private String ma;
    private String email;
    private String sdt;
    private String avatar;
    private Instant ngaySinh;
    private Boolean gioiTinh;
    private Long idGioHang;
}
