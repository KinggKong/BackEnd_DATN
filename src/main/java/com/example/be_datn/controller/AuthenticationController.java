package com.example.be_datn.controller;

import com.example.be_datn.config.jwtConfig.JwtProvider;
import com.example.be_datn.dto.Request.EmailRequest;
import com.example.be_datn.dto.Request.SignInRequest;
import com.example.be_datn.dto.Request.SignupRequest;
import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Response.SignInResponse;
import com.example.be_datn.service.IAuthService;
import com.example.be_datn.utils.LoginHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    IAuthService iAuthService;
    LoginHelper loginHelper;
    JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody SignInRequest signInRequest, HttpServletRequest request) {
        return iAuthService.login(signInRequest, request);
    }

    @PostMapping("/sign-up")
    public ApiResponse<?> signUp(@RequestBody SignupRequest signupRequest) {
        return iAuthService.registerAccount(signupRequest);
    }

    @PostMapping("/via-email")
    public ApiResponse<?> viaEmailToResgister(@RequestBody EmailRequest emailRequest) {
        return ApiResponse.builder()
                .data(iAuthService.handleSendCodeToMail(emailRequest.getEmail()))
                .message("Send code to maill successfulll")
                .build();
    }

    @GetMapping("/oauth/google")
    public void hanldeLoginWithGoogle(@RequestParam("code") String code,
                                      @RequestParam("scope") String scope,
                                      @RequestParam("authuser") String authUser,
                                      @RequestParam("prompt") String prompt,
                                      HttpServletResponse response) throws IOException {

        SignInResponse signInResponse = loginHelper.processGrantCode(code);
        if (signInResponse != null) {
            String redirectUrl = "http://localhost:5173/auth/login-success?token=" + signInResponse.getAccessToken();
            response.sendRedirect(redirectUrl);
        } else {
            String redirectUrl = "http://localhost:5173/auth/login-success";
            response.sendRedirect(redirectUrl);
        }
    }

    @GetMapping("/profile")
    public ApiResponse<?> getProfile() {
        return iAuthService.getProfile();
    }

}
