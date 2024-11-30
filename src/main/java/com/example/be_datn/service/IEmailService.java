package com.example.be_datn.service;

import com.example.be_datn.dto.Response.InfoOrder;
import jakarta.mail.MessagingException;

public interface IEmailService {
    String sendMailToUser(String to, String subject, String maHoaDon, InfoOrder infoOrder) throws MessagingException;

    String sendMailToAllUsers(String to, String subject, String body);
}
