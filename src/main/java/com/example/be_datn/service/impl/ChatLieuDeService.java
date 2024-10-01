package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.ChatLieuDeRequest;
import com.example.be_datn.dto.Response.ChatLieuDeResponse;
import com.example.be_datn.entity.ChatLieuDe;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.ChatLieuDeRepository;
import com.example.be_datn.service.IChatLieuDeService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ChatLieuDeService implements IChatLieuDeService {
    ChatLieuDeRepository chatLieuDeRepository;
    @Override
    public List<ChatLieuDe> getAll() {
        return chatLieuDeRepository.findAll();
    }

    @Override
    public Page<ChatLieuDeResponse> getByName(String name, Pageable pageable) {
        return chatLieuDeRepository.findChatLieuDeLikeName("%"+name+"%", pageable).map(ChatLieuDeResponse::fromChatLieuDe);
    }

    @Override
    public Page<ChatLieuDe> getAll(Pageable pageable) {
        return chatLieuDeRepository.findAll(pageable);
    }

    @Override
    public ChatLieuDeResponse create(ChatLieuDeRequest chatLieuDe) {
       if(chatLieuDeRepository.existsChatLieuDeByTenChatLieu(chatLieuDe.getTenChatLieu())){
          throw new AppException(ErrorCode.CHATLIEUDE_ALREADY_EXISTS);
       }
       ChatLieuDe chatLieuDe1 = ChatLieuDe.builder()
               .tenChatLieu(chatLieuDe.getTenChatLieu())
               .trangThai(chatLieuDe.getTrangThai())
               .build();
        ChatLieuDe savedChatLieuDe = chatLieuDeRepository.save(chatLieuDe1);
        return ChatLieuDeResponse.fromChatLieuDe(savedChatLieuDe);
    }

    @Override
    public ChatLieuDeResponse getById(Long id) {
        return chatLieuDeRepository.findById(id).map(ChatLieuDeResponse::fromChatLieuDe).orElseThrow(() -> new AppException(ErrorCode.CHATLIEUDE_NOT_FOUND));
    }

    @Override
    public ChatLieuDeResponse update(Long id, ChatLieuDeRequest chatLieuDe) {
        var chatLieuDe1 = chatLieuDeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CHATLIEUDE_NOT_FOUND));
        chatLieuDe1.setTenChatLieu(chatLieuDe.getTenChatLieu());
        chatLieuDe1.setTrangThai(chatLieuDe.getTrangThai());
        return ChatLieuDeResponse.fromChatLieuDe(chatLieuDeRepository.save(chatLieuDe1));

    }

    @Override
    public String delete(Long id) {
        if(chatLieuDeRepository.existsById(id)){
            chatLieuDeRepository.deleteById(id);
            return "Xóa thành công";
        }else {
            throw new AppException(ErrorCode.CHATLIEUDE_NOT_FOUND);
        }
    }
}
