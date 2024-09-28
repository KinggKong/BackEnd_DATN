package com.example.be_datn.service;


import com.example.be_datn.entity.ChatLieuDe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IChatLieuDeService {
    List<ChatLieuDe> getAll();
    List<ChatLieuDe> getByName(String name);

    Page<ChatLieuDe> getAll(Pageable pageable);

    ChatLieuDe create(ChatLieuDe chatLieuDe);

    ChatLieuDe getById(Long id);

    ChatLieuDe update(Long id, ChatLieuDe chatLieuDe);

    String delete(Long id);


}
