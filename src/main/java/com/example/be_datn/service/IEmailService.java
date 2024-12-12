package com.example.be_datn.service;

import com.example.be_datn.dto.Response.InfoOrder;
import jakarta.mail.MessagingException;

import java.util.concurrent.CompletableFuture;

public interface IEmailService {
    CompletableFuture<String> sendMailToUser(String to, String subject, String maHoaDon, InfoOrder infoOrder) throws MessagingException;

    String sendMailToAllUsers(String to, String subject, String body);
}
