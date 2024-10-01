package com.example.be_datn.service;

import com.example.be_datn.dto.Request.ChatLieuVaiRequest;
import com.example.be_datn.dto.Response.ChatLieuVaiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IChatLieuVaiService {

    Page<ChatLieuVaiResponse> getAllChatLieuVaiPageable(String tenChatLieu, Pageable pageable); // Phương thức phân trang

    ChatLieuVaiResponse createChatLieuVai(ChatLieuVaiRequest chatLieuVaiRequest); // Phương thức tạo mới chất liệu vải

    ChatLieuVaiResponse getChatLieuVaiById(Long id); // Phương thức lấy chất liệu vải theo ID

    ChatLieuVaiResponse updateChatLieuVai(Long idChatLieuVai, ChatLieuVaiRequest chatLieuVaiRequest); // Phương thức cập nhật chất liệu vải

    String deleteChatLieuVai(Long id); // Phương thức xóa chất liệu vải

}
