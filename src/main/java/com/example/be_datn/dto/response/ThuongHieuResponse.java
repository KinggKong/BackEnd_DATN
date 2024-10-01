package com.example.be_datn.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ThuongHieuResponse {
    private Long id;
    private String tenThuongHieu;
    private int trangThai;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
