package com.example.be_datn.service.impl;

import com.example.be_datn.config.AccountDetailsImpl;
import com.example.be_datn.dto.Request.TaiKhoanRequest;
import com.example.be_datn.dto.Response.TaiKhoanResponse;
import com.example.be_datn.entity.TaiKhoan;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.mapper.TaiKhoanMapper;
import com.example.be_datn.repository.TaiKhoanRepository;
import com.example.be_datn.service.ITaiKhoanService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaiKhoanService implements ITaiKhoanService, UserDetailsService {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 15;
    Random random = new Random();
    TaiKhoanRepository taiKhoanRepository;
    TaiKhoanMapper taiKhoanMapper;


    @Override
    public List<TaiKhoanResponse> getAllTaiKhoan() {
        List<TaiKhoan> taiKhoans = taiKhoanRepository.findAll();
        return taiKhoanMapper.toListTaiKhoanResponse(taiKhoans);
    }

    @Override
    public TaiKhoanResponse createTaiKhoan(TaiKhoanRequest taiKhoanRequest) {
        TaiKhoan newtaiKhoan = taiKhoanMapper.toTaiKhoan(taiKhoanRequest);
        if (taiKhoanRepository.findByTenDangNhap(taiKhoanRequest.getTenDangNhap()).isPresent()) {
            throw new AppException(ErrorCode.TEN_TAIKHOAN_EXISTED);
        }
        String ma = generateCodeAccount();
        if (!existTaiKhoan(ma)) newtaiKhoan.setMa(ma);
        else throw new AppException(ErrorCode.MA_TAIKHOAN_EXIST);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        newtaiKhoan.setMatKhau(passwordEncoder.encode(taiKhoanRequest.getMatKhau()));
        return taiKhoanMapper.toTaiKhoanResponse(taiKhoanRepository.save(newtaiKhoan));
    }

    @Override
    public TaiKhoanResponse getTaiKhoan(Long taiKhoanId) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(taiKhoanId).orElseThrow(() -> new AppException(ErrorCode.TAIKHOAN_NOT_FOUND));
        return taiKhoanMapper.toTaiKhoanResponse(taiKhoan);
    }

    @Override
    public String deleteTaiKhoan(Long taiKhoanId) {
        if (getTaiKhoan(taiKhoanId) != null) {
            taiKhoanRepository.deleteById(taiKhoanId);
            return "delete successfully";
        }
        return "delete failed";
    }

    @Override
    public TaiKhoanResponse updateTaiKhoan(TaiKhoanRequest taiKhoanRequest, Long taiKhoanId) {
        TaiKhoan taiKhoanExist = taiKhoanRepository.findById(taiKhoanId).orElseThrow(() -> new AppException(ErrorCode.TAIKHOAN_NOT_FOUND));
        taiKhoanMapper.updateTaiKhoan(taiKhoanExist, taiKhoanRequest);
        return taiKhoanMapper.toTaiKhoanResponse(taiKhoanRepository.save(taiKhoanExist));
    }

    @Override
    public boolean existTaiKhoan(String maTaiKhoan) {
        return taiKhoanRepository.existsTaiKhoanByMa(maTaiKhoan);
    }

    @Override
    public boolean existTenTaiKhoan(String tenTaiKhoan) {
        return false;
    }

    @Override
    public TaiKhoanResponse getTaiKhoanByIDOwner(String email) {
        return taiKhoanMapper.toTaiKhoanResponse(taiKhoanRepository.findByOwnerID(email).orElseThrow(() -> new AppException(ErrorCode.TAIKHOAN_NOT_FOUND)));
    }


    public String generateCodeAccount() {
        StringBuilder billCode = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            billCode.append(CHARACTERS.charAt(index));
        }
        return billCode.toString();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AccountDetailsImpl(taiKhoanRepository.findByEmailAndUsername(username).get());
    }
}
