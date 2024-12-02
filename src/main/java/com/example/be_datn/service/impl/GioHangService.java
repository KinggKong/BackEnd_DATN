package com.example.be_datn.service.impl;

import com.example.be_datn.entity.GioHang;
import com.example.be_datn.entity.KhachHang;
import com.example.be_datn.repository.GioHangRepository;
import com.example.be_datn.repository.TaiKhoanRepository;
import com.example.be_datn.service.IGioHangService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GioHangService implements IGioHangService {
    GioHangRepository gioHangRepository;
    TaiKhoanRepository taiKhoanRepository;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 10;
    private static final SecureRandom random = new SecureRandom();

    public static String generateCartCode() {
        StringBuilder cartCode = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            cartCode.append(CHARACTERS.charAt(randomIndex));
        }
        return cartCode.toString();
    }

    @Override
    public int themGioHang(Long idKhachHang) {
        KhachHang khachHang = new KhachHang();
        khachHang.setId(idKhachHang);

        GioHang gioHang = GioHang.builder()
                .khachHang(khachHang)
                .ma(generateCartCode())
                .build();
        gioHangRepository.save(gioHang);
        return 1;
    }
}
