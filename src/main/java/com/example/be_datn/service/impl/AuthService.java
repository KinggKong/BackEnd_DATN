package com.example.be_datn.service.impl;

import com.example.be_datn.config.AccountDetailsImpl;
import com.example.be_datn.config.jwtConfig.JwtProvider;
import com.example.be_datn.dto.Request.LogoutRequest;
import com.example.be_datn.dto.Request.SignInRequest;
import com.example.be_datn.dto.Request.SignupRequest;
import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Response.Profile;
import com.example.be_datn.dto.Response.SignInResponse;
import com.example.be_datn.dto.Response.TaiKhoanResponse;
import com.example.be_datn.entity.*;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.mapper.ProfileMapper;
import com.example.be_datn.mapper.TaiKhoanMapper;
import com.example.be_datn.repository.*;
import com.example.be_datn.service.IAuthService;
import com.example.be_datn.utils.SecurityUtils;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class AuthService implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtUtils;
    JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    ViaCodeService viaCodeService;
    TaiKhoanRepository taiKhoanRepository;
    TaiKhoanMapper taiKhoanMapper;
    private final KhachHangRepository khachHangRepository;
    private final VaiTroRepository vaiTroRepository;
    private final ViaCodeRepository viaCodeRepository;
    private final GioHangRepository gioHangRepository;
    private final ProfileMapper profileMapper;

    @Override
    public ResponseEntity<ApiResponse<SignInResponse>> login(SignInRequest signinRequest, HttpServletRequest request) {
        try {
            Authentication authentication = authenticateUser(signinRequest);
            AccountDetailsImpl accountDetails = (AccountDetailsImpl) authentication.getPrincipal();
            Long accountId = accountDetails.getAccount().getId();

            LocalDateTime now = LocalDateTime.now();


            String accountToken = jwtUtils.generateTokenByUsername(accountDetails.getUsername());
            GioHang gioHang = gioHangRepository.findByKhachHang_Id(accountDetails.getAccount().getOwnerID());

            SignInResponse signinResponse = SignInResponse.builder()
                    .id(accountId)
                    .type("Bearer")
                    .accessToken(accountToken)
                    .username(accountDetails.getUsername())
                    .email(accountDetails.getAccount().getEmail())
                    .isActive(accountDetails.getAccount().getTrangThai() == 1 ? true : false)
                    .roleName(accountDetails.getRoleName())
                    .idGioHang(gioHang.getId())
                    .build();

            ApiResponse<SignInResponse> apiResponse = ApiResponse.<SignInResponse>builder()
                    .data(signinResponse)
                    .message("Login success")
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            throw new AppException(ErrorCode.LOGIN_FAILED);
        }
    }

    @Override
    public ResponseEntity<ApiResponse<?>> logout(LogoutRequest logoutRequest) {
        try {
            ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                    .message("Logout successful")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            throw new AppException(ErrorCode.LOGOUT_FAILED);
        }
    }

    @Override
    public ApiResponse<TaiKhoanResponse> registerAccount(SignupRequest signupRequest) {
        if (viaCodeService.validateCode(signupRequest.getCode(), signupRequest.getEmail())) {
            if (taiKhoanRepository.existsByTenDangNhap(signupRequest.getUsername())) {
                throw new AppException(ErrorCode.USER_USERNAME_EXISTED);
            }
            if (taiKhoanRepository.existsByEmail(signupRequest.getEmail())) {
                throw new AppException(ErrorCode.ACCOUNT_EMAIL_EXISTED);
            }

            KhachHang khachHang = KhachHang.builder()
                    .ten(signupRequest.getName())
                    .ma(generateAccountCode())
                    .email(signupRequest.getEmail())
                    .trangThai(1)
                    .build();

            KhachHang insertKhachHang = khachHangRepository.saveAndFlush(khachHang);

            if (insertKhachHang == null) {
                throw new AppException(ErrorCode.USER_CANT_CREATE_USER);
            }

            VaiTro role = vaiTroRepository.findByTenVaiTro("ROLE_USER");
            if (role == null) {
                throw new AppException(ErrorCode.ROLE_NOT_FOUND);
            }

            TaiKhoan account = TaiKhoan.builder()
                    .ownerID(insertKhachHang.getId())
                    .ma(generateAccountCode())
                    .tenDangNhap(signupRequest.getUsername())
                    .matKhau(passwordEncoder.encode(signupRequest.getPassword()))
                    .email(signupRequest.getEmail())
                    .vaiTro(role)
                    .trangThai(1)
                    .build();

            return ApiResponse.<TaiKhoanResponse>builder()
                    .data(taiKhoanMapper.toTaiKhoanResponse(taiKhoanRepository.save(account)))
                    .message("register successfully")
                    .build();
        } else {
            throw new AppException(ErrorCode.REGISTER_ACCOUNT_FAILED);
        }
    }

    @Override
    public int sendCodeToEmail(String to, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            javaMailSender.send(message);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public ViaCode insertCode(int code, String email) {
        ViaCode vi = ViaCode.builder()
                .viaCode(code)
                .email(email.trim())
                .createdAt(LocalDateTime.now())
                .build();
        return viaCodeRepository.save(vi);
    }

    @Override
    public String handleSendCodeToMail(String email) {
        try {
            int code = generateCode();
            int result = sendCodeToEmail(email, "Code to verifiy accout", "Code to sign up for you is: " + code);
            insertCode(code, email);
            return "send code to email successfully";
        } catch (Exception e) {
            return "send code to email failed" + e.getMessage();
        }
    }

    @Override
    public ApiResponse<?> getProfile() {
        Optional<KhachHang> khachHang = khachHangRepository.findById(SecurityUtils.getCurrentUserId());
        if (khachHang.isEmpty()) {
            throw new AppException(ErrorCode.KHACH_HANG_NOT_FOUND);
        }
        GioHang gioHang = gioHangRepository.findByKhachHang_Id(khachHang.get().getId());
        return ApiResponse.<Profile>builder()
                .data(profileMapper.toProfile(khachHang.get(), gioHang.getId()))
                .build();
    }

    private Authentication authenticateUser(SignInRequest signinRequest) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword()));
    }

    public int generateCode() {
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000);
        return randomNumber;
    }

    public static String generateAccountCode() {
        String uuid = UUID.randomUUID().toString();
        String accountCode = uuid.replace("-", "").substring(0, 10).toUpperCase();
        return accountCode;
    }
}
