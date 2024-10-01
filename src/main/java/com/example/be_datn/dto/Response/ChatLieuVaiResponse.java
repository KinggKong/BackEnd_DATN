package com.example.be_datn.dto.Response;

import com.example.be_datn.entity.ChatLieuVai; // Thay đổi thành entity tương ứng
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatLieuVaiResponse {
    Long id;
    String tenChatLieuVai; // Tên chất liệu vải
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDateTime updatedAt;
    int trangThai;

    public static ChatLieuVaiResponse fromChatLieuVai(ChatLieuVai chatLieuVai) {
        return ChatLieuVaiResponse.builder()
                .id(chatLieuVai.getId())
                .tenChatLieuVai(chatLieuVai.getTenChatLieuVai())
                .createdAt(chatLieuVai.getCreated_at())
                .updatedAt(chatLieuVai.getUpdated_at())
                .trangThai(chatLieuVai.getTrangThai())
                .build();
    }
}
