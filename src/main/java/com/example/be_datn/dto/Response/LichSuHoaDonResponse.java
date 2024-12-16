package com.example.be_datn.dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class LichSuHoaDonResponse {

    LocalDateTime createdAt;

    String ghiChu;

    String trangThai;

    String createdBy;
}
