package com.example.be_datn.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_lieu_vai")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatLieuVai extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "ten_chat_lieu_vai")
    String tenChatLieuVai;

    @Column(name = "trang_thai")
    int trangThai;

}
