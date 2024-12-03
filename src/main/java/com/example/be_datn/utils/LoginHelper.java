package com.example.be_datn.utils;


import com.example.be_datn.config.jwtConfig.JwtProvider;
import com.example.be_datn.dto.Request.ModelAccountGoogle;
import com.example.be_datn.dto.Response.SignInResponse;
import com.example.be_datn.entity.GioHang;
import com.example.be_datn.entity.KhachHang;
import com.example.be_datn.entity.TaiKhoan;
import com.example.be_datn.entity.VaiTro;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.mapper.TaiKhoanMapper;
import com.example.be_datn.repository.GioHangRepository;
import com.example.be_datn.repository.KhachHangRepository;
import com.example.be_datn.repository.TaiKhoanRepository;
import com.example.be_datn.repository.VaiTroRepository;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Component
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginHelper {
    KhachHangRepository userRepository;
    PasswordEncoder passwordEncoder;
    TaiKhoanRepository accountRepository;
    JwtProvider jwtProvider;
    VaiTroRepository roleRepository;
    TaiKhoanMapper accountMapper;
    private final GioHangRepository gioHangRepository;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String clientId;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    String clientSecret;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    String redirectUrl;

    private String getOauthAccessTokenGoogle(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("redirect_uri", redirectUrl);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile");
        params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email");
        params.add("scope", "openid");
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);

        String url = "https://oauth2.googleapis.com/token";
        String response = restTemplate.postForObject(url, requestEntity, String.class);
        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);

        return jsonObject.get("access_token").toString().replace("\"", "");
    }

    public SignInResponse processGrantCode(String code) {
        String accessToken = getOauthAccessTokenGoogle(code);

        ModelAccountGoogle modelAccountGoogle = getProfileDetailsGoogle(accessToken);
        Optional<TaiKhoan> account = accountRepository.findByEmail(modelAccountGoogle.getEmail());

        if (account.isPresent()) {
            TaiKhoan acc = account.get();
            String accountToken = jwtProvider.generateTokenByUsername(acc.getTenDangNhap());

            return SignInResponse.builder()
                    .id(acc.getId())
                    .type("Bearer")
                    .accessToken(accountToken)
                    .username(acc.getTenDangNhap())
                    .email(acc.getEmail())
                    .isActive(acc.getTrangThai() == 1 ? true : false)
                    .roleName(acc.getVaiTro().getTenVaiTro())
                    .build();

        }

        TaiKhoan accountRegister = registerAccount(modelAccountGoogle);
        String accountToken = jwtProvider.generateTokenByUsername(accountRegister.getTenDangNhap());

        return SignInResponse.builder()
                .id(accountRegister.getId())
                .type("Bearer")
                .accessToken(accountToken)
                .username(accountRegister.getTenDangNhap())
                .email(accountRegister.getEmail())
                .isActive(accountRegister.getTrangThai() == 1 ? true : false)
                .roleName(accountRegister.getVaiTro().getTenVaiTro())
                .build();

    }

    private ModelAccountGoogle getProfileDetailsGoogle(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

        String url = "https://www.googleapis.com/oauth2/v2/userinfo";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        JsonObject jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class);
        String random = generateAccountCode().substring(0, 5);

        return ModelAccountGoogle.builder()
                .name(jsonObject.get("name").toString().replace("\"", ""))
                .username(random + "_" + jsonObject.get("name").toString().replace("\"", ""))
                .email(jsonObject.get("email").toString().replace("\"", ""))
                .password(UUID.randomUUID().toString())
                .build();

    }

    public TaiKhoan registerAccount(ModelAccountGoogle modelAccountGoogle) {

        if (accountRepository.existsByEmail(modelAccountGoogle.getEmail())) {
            throw new AppException(ErrorCode.ACCOUNT_EMAIL_EXISTED);
        }

        KhachHang user = KhachHang.builder()
                .ten(modelAccountGoogle.getName())
                .ma(generateAccountCode())
                .email(modelAccountGoogle.getEmail())
                .trangThai(1)
                .build();

        KhachHang insertKhachHang = userRepository.saveAndFlush(user);

        if (insertKhachHang == null) {
            throw new AppException(ErrorCode.USER_CANT_CREATE_USER);
        }

        VaiTro role = roleRepository.findByTenVaiTro("ROLE_USER");
        if (role == null) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }

        TaiKhoan account = TaiKhoan.builder()
                .ownerID(insertKhachHang.getId())
                .ma(generateAccountCode())
                .tenDangNhap(modelAccountGoogle.getUsername())
                .matKhau(passwordEncoder.encode(modelAccountGoogle.getPassword()))
                .email(modelAccountGoogle.getEmail())
                .vaiTro(role)
                .trangThai(1)
                .build();

        GioHang gioHang = GioHang.builder()
                .ma(generateAccountCode())
                .khachHang(insertKhachHang)
                .trangThai(1)
                .build();
        gioHangRepository.saveAndFlush(gioHang);
        return accountRepository.saveAndFlush(account);
    }

    public static String generateAccountCode() {
        String uuid = UUID.randomUUID().toString();
        String accountCode = uuid.replace("-", "").substring(0, 10).toUpperCase();
        return accountCode;
    }
}
