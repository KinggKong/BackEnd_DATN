package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.ChatLieuVaiRequest;
import com.example.be_datn.dto.Response.ChatLieuVaiResponse;
import com.example.be_datn.entity.ChatLieuVai;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.ChatLieuVaiRepository;
import com.example.be_datn.service.IChatLieuVaiService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ChatLieuVaiService implements IChatLieuVaiService { // Thay đổi tên lớp
    ChatLieuVaiRepository chatLieuVaiRepository; // Thay đổi repository

    @Override
    public Page<ChatLieuVaiResponse> getAllChatLieuVaiPageable(String tenChatLieu, Pageable pageable) {
        return chatLieuVaiRepository.findChatLieuVaiByTenChatLieuLike("%" + tenChatLieu + "%", pageable)
                .map(ChatLieuVaiResponse::fromChatLieuVai);
    }

    @Override
    public ChatLieuVaiResponse createChatLieuVai(ChatLieuVaiRequest chatLieuVaiRequest) {
        if (chatLieuVaiRepository.existsChatLieuVaiByTenChatLieuVai(chatLieuVaiRequest.getTenChatLieuVai())) {
            throw new AppException(ErrorCode.CHATLIEUVAI_ALREADY_EXISTS);
        }
        ChatLieuVai chatLieuVai = ChatLieuVai.builder()
                .tenChatLieuVai(chatLieuVaiRequest.getTenChatLieuVai())
                .trangThai(chatLieuVaiRequest.getTrangThai())
                .build();
        ChatLieuVai savedChatLieuVai = chatLieuVaiRepository.save(chatLieuVai);
        return ChatLieuVaiResponse.fromChatLieuVai(savedChatLieuVai);
    }

    @Override
    public ChatLieuVaiResponse getChatLieuVaiById(Long id) {
        return chatLieuVaiRepository.findById(id)
                .map(ChatLieuVaiResponse::fromChatLieuVai)
                .orElseThrow(() -> new AppException(ErrorCode.CHATLIEUVAI_NOT_FOUND));
    }

    @Override
    public ChatLieuVaiResponse updateChatLieuVai(Long idChatLieuVai, ChatLieuVaiRequest chatLieuVaiRequest) {
        var chatLieuVai = chatLieuVaiRepository.findById(idChatLieuVai)
                .orElseThrow(() -> new AppException(ErrorCode.CHATLIEUVAI_NOT_FOUND));
        chatLieuVai.setTenChatLieuVai(chatLieuVaiRequest.getTenChatLieuVai());
        chatLieuVai.setTrangThai(chatLieuVaiRequest.getTrangThai());
        return ChatLieuVaiResponse.fromChatLieuVai(chatLieuVaiRepository.save(chatLieuVai));
    }

    @Override
    public String deleteChatLieuVai(Long id) {
        getChatLieuVaiById(id);
        chatLieuVaiRepository.deleteById(id);
        return "deleted successfully";
    }
}
