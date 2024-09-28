package com.example.be_datn.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "ChatLieuDe")
public class ChatLieuDe extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;
    @Column(name = "ten_chat_lieu")
     String tenChatLieu;
    @Column(name = "trang_thai")
     int trangThai;
}
