package com.example.be_datn.repository;

import com.example.be_datn.entity.ChatLieuDe;
import com.example.be_datn.entity.MauSac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ChatLieuDeRepository extends JpaRepository<ChatLieuDe,Long> {
    boolean existsChatLieuDeByTenChatLieu(String tenChatLieu);


    @Query(value = "select c from ChatLieuDe c where c.tenChatLieu like :ten")
    Page<ChatLieuDe> findChatLieuDeLikeName(String ten, Pageable pageable);
}
