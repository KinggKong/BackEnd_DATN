package com.example.be_datn.dto.Response;

import com.example.be_datn.entity.MauSac;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MauSacResponse {
    Long id;
    String tenMau;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDateTime updatedAt;
    int trangThai;

    public static MauSacResponse fromMauSac(MauSac mauSac) {
        return MauSacResponse.builder()
                .id(mauSac.getId())
                .tenMau(mauSac.getTenMau())
                .createdAt(mauSac.getCreated_at())
                .updatedAt(mauSac.getUpdated_at())
                .trangThai(mauSac.getTrangThai())
                .build();
    }
}
