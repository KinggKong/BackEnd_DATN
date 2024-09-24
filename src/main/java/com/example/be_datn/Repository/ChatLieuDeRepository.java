package com.example.be_datn.Repository;

import com.example.be_datn.Entity.ChatLieuDe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface ChatLieuDeRepository extends JpaRepository<ChatLieuDe,Long> {
    boolean existsChatLieuDeByTenChatLieu(String tenChatLieu);
}
