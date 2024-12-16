package com.example.be_datn.service;

import com.example.be_datn.dto.Request.ChangePasswordRequest;
import com.example.be_datn.dto.Request.LogoutRequest;
import com.example.be_datn.dto.Request.SignInRequest;
import com.example.be_datn.dto.Request.SignupRequest;
import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Response.SignInResponse;
import com.example.be_datn.dto.Response.TaiKhoanResponse;
import com.example.be_datn.entity.ViaCode;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    ResponseEntity<ApiResponse<SignInResponse>> login(SignInRequest signinRequest, HttpServletRequest request);
    ResponseEntity<ApiResponse<SignInResponse>> loginAdmin(SignInRequest signinRequest, HttpServletRequest request);
    ResponseEntity<ApiResponse<?>> logout(LogoutRequest logoutRequest);
    ApiResponse<TaiKhoanResponse> registerAccount(SignupRequest signupRequest);
    int sendCodeToEmail(String to, String subject, String content);

    ViaCode insertCode(int code, String email);

    String handleSendCodeToMail(String email);

    ApiResponse<?> getProfile();

    ApiResponse<?> getProfileAdmin();

    String sendTokenForgotPassword(String email) throws MessagingException;

    TaiKhoanResponse resetPassword(ChangePasswordRequest changePasswrodRequest);
}
