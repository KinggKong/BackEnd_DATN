package com.example.be_datn.repository;

import com.example.be_datn.entity.ChatLieuVai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatLieuVaiRepository extends JpaRepository<ChatLieuVai, Long> {
    boolean existsChatLieuVaiByTenChatLieuVai(String tenChatLieuVai); // Kiểm tra sự tồn tại của chất liệu

    @Query(value = "select c from ChatLieuVai c where c.tenChatLieuVai like :tenChatLieu") // Tìm kiếm theo tên chất liệu
    Page<ChatLieuVai> findChatLieuVaiByTenChatLieuLike(String tenChatLieu, Pageable pageable);
}
