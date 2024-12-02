package com.example.be_datn.config.jwtConfig;

import com.example.be_datn.config.AccountDetailsImpl;
import com.example.be_datn.entity.TaiKhoan;
import com.example.be_datn.repository.TaiKhoanRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
}
