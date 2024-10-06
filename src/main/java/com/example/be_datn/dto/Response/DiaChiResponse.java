package com.example.be_datn.dto.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DiaChiResponse {
    private Long id;
    private Long id_khach_hang;
    private String dia_chi_cu_the;
    private String sdt;
    private String tinh;
    private String quan;
    private String huyen;
    private String loai_dia_chi;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "created_at")
    private LocalDateTime created_at;
    @Column(name = "updated_at")
    private LocalDateTime updated_at;
}
