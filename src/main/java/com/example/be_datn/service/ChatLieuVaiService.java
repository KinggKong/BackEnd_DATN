package com.example.be_datn.service;

import com.example.be_datn.entity.ChatLieuVai;
import com.example.be_datn.model.request.ChatLieuVaiRequest;
import com.example.be_datn.model.response.ChatLuongVaiResponse;
import org.springframework.data.domain.Page;

public interface ChatLieuVaiService {
    Page<ChatLuongVaiResponse> gets(int page, int size);
    ChatLuongVaiResponse get(int id);
    void create(ChatLieuVaiRequest request);
    void update(ChatLieuVaiRequest request, int id);
    void delete(int id);
}
