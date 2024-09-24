package com.example.be_datn.repository;

import com.example.be_datn.entity.ChatLieuVai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatLieuVaiRepository extends JpaRepository<ChatLieuVai, Integer>, CrudRepository<ChatLieuVai, Integer> {
    Optional<ChatLieuVai> findByTenChatLieu(String tenChatLieu);
    Optional<ChatLieuVai> findByIdAndTenChatLieu(int id, String tenChatLieu);
}
