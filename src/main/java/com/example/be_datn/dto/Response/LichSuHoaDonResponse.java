package com.example.be_datn.dto.Response;

import com.example.be_datn.entity.HoaDon;
import com.example.be_datn.entity.NhanVien;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
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
}
