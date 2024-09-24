package com.example.be_datn.service.impl;

import com.example.be_datn.entity.ChatLieuVai;
import com.example.be_datn.exception.BusinessException;
import com.example.be_datn.model.request.ChatLieuVaiRequest;
import com.example.be_datn.model.response.ChatLuongVaiResponse;
import com.example.be_datn.repository.ChatLieuVaiRepository;
import com.example.be_datn.service.ChatLieuVaiService;
import com.example.be_datn.utils.ErrorCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ChatLieuVaiServiceImpl implements ChatLieuVaiService {
    private final ChatLieuVaiRepository chatLieuVaiRepository;

    private final EntityManager entityManager;
    @Override
    public Page<ChatLuongVaiResponse> gets(int page, int size) {
        var pageable = PageRequest.of(page, size);
        return chatLieuVaiRepository.findAll(pageable).map(ChatLuongVaiResponse::from);
    }

    @Override
    public ChatLuongVaiResponse get(int id) {
        return chatLieuVaiRepository.findById(id).map(ChatLuongVaiResponse::from).orElse(null);
    }

    @Override
    public void create(ChatLieuVaiRequest request) {
        var chatLieuVaiOpt = chatLieuVaiRepository.findByTenChatLieu(request.getTenChatLieu());
        if (chatLieuVaiOpt.isPresent()) {
            throw new BusinessException(ErrorCode.CHAT_LIEU_VAI_EXIST);
        }
        var chatLieuVai = new ChatLieuVai()
                .setTenChatLieu(request.getTenChatLieu())
                .setTrangThai(1)
                .setCreatedAt(new Date());
        try {
           chatLieuVaiRepository.save(chatLieuVai);
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new BusinessException(ErrorCode.CHAT_LIEU_VAI_EXIST);
            }
        }
    }

    @Override
    public void update(ChatLieuVaiRequest request, int id) {
        var chatLieuVai = chatLieuVaiRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.CHAT_LIEU_VAI_NOT_EXIST));
        chatLieuVai.setTenChatLieu(request.getTenChatLieu())
                .setTrangThai(request.getTrangThai())
                .setUpdatedAt(new Date());
        try {
            chatLieuVaiRepository.save(chatLieuVai);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        var chatLieuVai = chatLieuVaiRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.CHAT_LIEU_VAI_NOT_EXIST));
        chatLieuVai.setTrangThai(0);
        try {
            chatLieuVaiRepository.save(chatLieuVai);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
