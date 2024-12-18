package com.example.be_datn.service.impl;

import com.example.be_datn.config.AccountDetailsImpl;
import com.example.be_datn.config.jwtConfig.JwtProvider;
import com.example.be_datn.dto.Request.ChangePasswordRequest;
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
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.mail.MessagingException;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private final JwtProvider jwtProvider;
    private final NhanVienRepository nhanVienRepository;

    @Override
    public ResponseEntity<ApiResponse<SignInResponse>> login(SignInRequest signinRequest, HttpServletRequest request) {
        try {
            Authentication authentication = authenticateUser(signinRequest);
            AccountDetailsImpl accountDetails = (AccountDetailsImpl) authentication.getPrincipal();
            if (!accountDetails.getRoleName().equals("ROLE_USER")) {
                throw new AppException(ErrorCode.LOGIN_FAILED);
            }

            if (taiKhoanRepository.checkIsActive(signinRequest.getUsername()).isEmpty()) {
                throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);
            }
            if (khachHangRepository.checkIsActive(signinRequest.getUsername()).isEmpty()) {
                throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);
            }

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
    public ResponseEntity<ApiResponse<SignInResponse>> loginAdmin(SignInRequest signinRequest, HttpServletRequest request) {


        try {
            Authentication authentication = authenticateUser(signinRequest);
            AccountDetailsImpl accountDetails = (AccountDetailsImpl) authentication.getPrincipal();
            if (accountDetails.getRoleName().equals("ROLE_USER")) {
                throw new AppException(ErrorCode.LOGIN_FAILED);
            }

            if (nhanVienRepository.checkIsActive(signinRequest.getUsername()).isEmpty()) {
                throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);
            }

            Long accountId = accountDetails.getAccount().getId();

            LocalDateTime now = LocalDateTime.now();


            String accountToken = jwtUtils.generateTokenByUsername(accountDetails.getUsername());

            SignInResponse signinResponse = SignInResponse.builder()
                    .id(accountId)
                    .type("Bearer")
                    .accessToken(accountToken)
                    .username(accountDetails.getUsername())
                    .email(accountDetails.getAccount().getEmail())
                    .isActive(accountDetails.getAccount().getTrangThai() == 1 ? true : false)
                    .roleName(accountDetails.getRoleName())
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

            GioHang gioHang = GioHang.builder()
                    .ma(generateAccountCode())
                    .khachHang(insertKhachHang)
                    .trangThai(1)
                    .build();
            gioHangRepository.saveAndFlush(gioHang);

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
        if (!isValidEmail(email.trim())) {
            throw new AppException(ErrorCode.EMAIL_INCORRECT_FORMAT);
        }
        if (taiKhoanRepository.existsByEmail(email.trim())) {
            throw new AppException(ErrorCode.ACCOUNT_EMAIL_EXISTED);
        }
        try {
            int code = generateCode();
            int result = sendCodeToEmail(email, "Code to verifiy accout", "Code to sign up for you is: " + code);
            insertCode(code, email);
            return "send code to email successfully";
        } catch (Exception e) {
            return "send code to email failed" + e.getMessage();
        }
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public ApiResponse<?> getProfile() {
        Optional<KhachHang> khachHang = khachHangRepository.findById(SecurityUtils.getCurrentUserId());
        if (khachHang.isEmpty()) {
            throw new AppException(ErrorCode.KHACH_HANG_NOT_FOUND);
        }
        String role = SecurityUtils.getCurrentRole();
        GioHang gioHang = gioHangRepository.findByKhachHang_Id(khachHang.get().getId());
        return ApiResponse.<Profile>builder()
                .data(profileMapper.toProfile(khachHang.get(), gioHang.getId(), role))
                .build();
    }

    @Override
    public ApiResponse<?> getProfileAdmin() {
        Optional<NhanVien> nhanVien = nhanVienRepository.findById(SecurityUtils.getCurrentUserId());
        String role = SecurityUtils.getCurrentRole();
        if (nhanVien.isEmpty()) {
            throw new AppException(ErrorCode.NHANVIEN_NOT_FOUND);
        }
        return ApiResponse.<Profile>builder()
                .data(profileMapper.toProfileAdmin(nhanVien.get(), role))
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

    @Override
    public String sendTokenForgotPassword(String email) throws MessagingException {
        Optional<TaiKhoan> optional = taiKhoanRepository.findByEmail(email);

        if (!isValidEmail(email.trim())) {
            throw new AppException(ErrorCode.EMAIL_INCORRECT_FORMAT);
        }
        if (optional.isEmpty()) {
            throw new AppException(ErrorCode.TAIKHOAN_NOT_FOUND);
        }
        TaiKhoan account = optional.get();
        String token = jwtProvider.generateForgotPasswordTokenByUsername(account.getTenDangNhap());

        account.setForgotPasswordToken(token);
        taiKhoanRepository.saveAndFlush(account);

        String subtitle = "Forgot Password";
        String passwordResetLink = "http://localhost:5173/auth/reset-password?token=" + token;
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>Password Reset</title>\n" +
                "  <style>\n" +
                "    body {\n" +
                "      font-family: Arial, sans-serif;\n" +
                "      background-color: #f4f4f4;\n" +
                "      color: #333;\n" +
                "      margin: 0;\n" +
                "      padding: 0;\n" +
                "    }\n" +
                "    .container {\n" +
                "      max-width: 600px;\n" +
                "      margin: 0 auto;\n" +
                "      background-color: #ffffff;\n" +
                "      padding: 20px;\n" +
                "      border-radius: 5px;\n" +
                "      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"container\">\n" +
                "    <h2>Password Reset Request</h2>\n" +
                "    <p>Hello,</p>\n" +
                "    <p>We received a request to reset your password. Click the button below to set a new password:</p>\n" +
                "    <p><a href=\"{{passwordResetLink}}\" style=\"display: inline-block; padding: 10px 20px; color: #ffffff; background-color: #007bff; border-radius: 5px; text-decoration: none;\">Reset Password</a></p>\n" +
                "    <p>If you didn’t request a password reset, please ignore this email.</p>\n" +
                "    <p>Thank you, <br>3HST Shooes Shop</p>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>\n";


        html = html.replace("{{passwordResetLink}}", passwordResetLink);

        helper.setTo(email);
        helper.setSubject(subtitle);
        helper.setText(html, true);

        javaMailSender.send(message);
        return "send token to reset password successfully";
    }

    @Override
    public TaiKhoanResponse resetPassword(ChangePasswordRequest changePasswrodRequest) {
        String username = jwtProvider.getKeyByValueFromJWT(jwtProvider.getJWT_SECRET_FORGOT_PASSWORD_TOKEN(), "username", changePasswrodRequest.getToken(), String.class);
        Optional<TaiKhoan> optional = taiKhoanRepository.findByEmailAndUsername(username);
        if (optional.isEmpty()) {
            throw new AppException(ErrorCode.TOKEN_RESET_PASSWORD_INVALID);
        }
        TaiKhoan account = optional.get();

        if (account.getForgotPasswordToken() == null) {
            throw new AppException(ErrorCode.TOKEN_RESET_PASSWORD_INVALID);
        }

        if (!validateToken(jwtProvider.getJWT_SECRET_FORGOT_PASSWORD_TOKEN(), changePasswrodRequest.getToken())) {
            throw new AppException(ErrorCode.TOKEN_RESET_PASSWORD_INVALID);
        }

        Date expDate = jwtProvider.getKeyByValueFromJWT(
                jwtProvider.getJWT_SECRET_FORGOT_PASSWORD_TOKEN(),
                "exp",
                changePasswrodRequest.getToken(),
                Date.class
        );

        if (expDate == null) {
            throw new AppException(ErrorCode.TOKEN_RESET_PASSWORD_INVALID);
        }
        LocalDateTime exp = expDate.toInstant()
                .atZone(ZoneOffset.UTC)
                .toLocalDateTime();

        if (expDate.toInstant().isBefore(Instant.now())) {
            throw new AppException(ErrorCode.TOKEN_RESET_PASSWORD_INVALID);
        }

        account.setMatKhau(passwordEncoder.encode(changePasswrodRequest.getNewPasswrod()));
        account.setForgotPasswordToken(null);
        return taiKhoanMapper.toTaiKhoanResponse(taiKhoanRepository.save(account));
    }

    public boolean validateToken(String jwtTokenSecret, String token) {
        try {
            Jwts.parser().setSigningKey(jwtTokenSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

}
