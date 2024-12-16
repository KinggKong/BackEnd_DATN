package com.example.be_datn.config.jwtConfig;

import com.example.be_datn.config.AccountDetailsImpl;
import com.example.be_datn.entity.TaiKhoan;
import com.example.be_datn.repository.TaiKhoanRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    @Value("${jwt.SECRET_KEY}")
    private String JWT_SECRET;
    @Value("${jwt.JWT_EXPIRATION}")
    private int JWT_EXPIRATION;
    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    @Value("${jwt.JWT_EXPIRATION_FORGOT_PASSWORD_TOKEN}")
    private int JWT_EXPIRATION_FORGOT_PASSWORD_TOKEN;

    @Getter
    @Value("${jwt.SECRET_FORGOT_PASSWORD_TOKEN_KEY}")
    private String JWT_SECRET_FORGOT_PASSWORD_TOKEN;

    public String generateToken(AccountDetailsImpl customDetailService) {
        return this.generateTokenByUsername(customDetailService.getUsername());
    }

    public String generateTokenByUsername(String Username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        TaiKhoan taiKhoan = taiKhoanRepository.findByEmailAndUsername(Username).get();
        return Jwts.builder()
                .setSubject(Long.toString(taiKhoan.getId()))
                .claim("username", taiKhoan.getEmail())
                .claim("role", taiKhoan.getVaiTro().getTenVaiTro())
                .claim("idGioHang", taiKhoan.getId())
                .setExpiration(expiryDate)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }


    public String generateForgotPasswordTokenByUsername(String Username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_FORGOT_PASSWORD_TOKEN);
        TaiKhoan account = taiKhoanRepository.findByEmailAndUsername(Username).get();
        return Jwts.builder()
                .setSubject(Long.toString(account.getId()))
                .claim("username", account.getTenDangNhap())
                .claim("token_type",2)
                .setExpiration(expiryDate)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_FORGOT_PASSWORD_TOKEN)
                .compact();
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public String getKeyByValueFromJWT(String key, String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.get(key, String.class);
    }

    public <T> T getKeyByValueFromJWT(String jwtSecretKey, String key, String token, Class<T> clazz) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.get(key, clazz);
    }
}
