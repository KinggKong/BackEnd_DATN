package com.example.be_datn.Service.impl;

import com.example.be_datn.Entity.ChatLieuDe;
import com.example.be_datn.Exception.AppException;
import com.example.be_datn.Exception.ErrorCode;
import com.example.be_datn.Repository.ChatLieuDeRepository;
import com.example.be_datn.Service.IChatLieuDeService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public List<ChatLieuDe> getByName(String name) {
        return chatLieuDeRepository.findByTenChatLieuContaining(name);
    }

    @Override
    public Page<ChatLieuDe> getAll(Pageable pageable) {
        return chatLieuDeRepository.findAll(pageable);
    }

    @Override
    public ChatLieuDe create(ChatLieuDe chatLieuDe) {
       if(chatLieuDeRepository.existsChatLieuDeByTenChatLieu(chatLieuDe.getTenChatLieu())){
          throw new AppException(ErrorCode.CHATLIEUDE_ALREADY_EXISTS);
       }else {
              return chatLieuDeRepository.save(chatLieuDe);
       }
    }

    @Override
    public ChatLieuDe getById(Long id) {
        return chatLieuDeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CHATLIEUDE_NOT_FOUND));
    }

    @Override
    public ChatLieuDe update(Long id, ChatLieuDe chatLieuDe) {
        Optional<ChatLieuDe> chatLieuDeOptional = chatLieuDeRepository.findById(id);
        if (chatLieuDeOptional.isPresent()) {
            ChatLieuDe chatLieuDeUpdate = chatLieuDeOptional.get();
            chatLieuDeUpdate.setTenChatLieu(chatLieuDe.getTenChatLieu());
            chatLieuDeUpdate.setTrangThai(chatLieuDe.getTrangThai());
            chatLieuDeUpdate.setUpdated_at(chatLieuDe.getUpdated_at());
            return chatLieuDeRepository.save(chatLieuDeUpdate);
        } else {
            throw new AppException(ErrorCode.CHATLIEUDE_NOT_FOUND);
        }
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
