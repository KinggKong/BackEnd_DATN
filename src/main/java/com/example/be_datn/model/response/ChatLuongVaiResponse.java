package com.example.be_datn.model.response;

import com.example.be_datn.entity.ChatLieuVai;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class ChatLuongVaiResponse {
    private int id;
    private String tenChatLieu;
    private int trangThai;
    private Date createdAt;
    private Date updatedAt;

    public static ChatLuongVaiResponse from(ChatLieuVai chatLieuVai) {
        var response = new ChatLuongVaiResponse();
        response.setId(chatLieuVai.getId());
        response.setTenChatLieu(chatLieuVai.getTenChatLieu());
        response.setTrangThai(chatLieuVai.getTrangThai());
        response.setCreatedAt(chatLieuVai.getCreatedAt());
        response.setUpdatedAt(chatLieuVai.getUpdatedAt());
        return response;
    }
}
