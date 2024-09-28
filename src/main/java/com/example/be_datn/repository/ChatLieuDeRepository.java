package com.example.be_datn.repository;

import com.example.be_datn.entity.ChatLieuDe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ChatLieuDeRepository extends JpaRepository<ChatLieuDe,Long> {
    boolean existsChatLieuDeByTenChatLieu(String tenChatLieu);


    List<ChatLieuDe> findByTenChatLieuContaining(String tenChatLieu);
}
