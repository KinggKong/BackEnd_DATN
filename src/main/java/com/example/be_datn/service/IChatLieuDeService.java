package com.example.be_datn.service;


import com.example.be_datn.dto.Request.ChatLieuDeRequest;
import com.example.be_datn.dto.Response.ChatLieuDeResponse;
import com.example.be_datn.entity.ChatLieuDe;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IChatLieuDeService {
    List<ChatLieuDe> getAll();
    Page<ChatLieuDeResponse> getByName(String name, Pageable pageable);

    Page<ChatLieuDe> getAll(Pageable pageable);

    ChatLieuDeResponse create( ChatLieuDeRequest chatLieuDe);

    ChatLieuDeResponse getById(Long id);

    ChatLieuDeResponse update(Long id,  ChatLieuDeRequest chatLieuDe);

    String delete(Long id);


}
