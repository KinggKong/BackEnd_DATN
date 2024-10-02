package com.example.be_datn.dto.Response;

import com.example.be_datn.entity.ChatLieuDe;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatLieuDeResponse {
    Long id;
    String tenChatLieu;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDateTime updatedAt;
    int trangThai;

    public static ChatLieuDeResponse fromChatLieuDe(ChatLieuDe chatLieuDe) {
        return ChatLieuDeResponse.builder()
                .id(chatLieuDe.getId())
                .tenChatLieu(chatLieuDe.getTenChatLieu())
                .createdAt(chatLieuDe.getCreated_at())
                .updatedAt(chatLieuDe.getUpdated_at())
                .trangThai(chatLieuDe.getTrangThai())
                .build();
    }
}
