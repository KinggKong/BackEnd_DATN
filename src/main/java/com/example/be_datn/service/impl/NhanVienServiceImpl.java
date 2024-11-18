package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.NhanVienRequest;
import com.example.be_datn.dto.Request.NhanVienUpdate;
import com.example.be_datn.dto.Response.NhanVienResponse;
import com.example.be_datn.entity.NhanVien;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.mapper.NhanVienEntity2Response;
import com.example.be_datn.repository.NhanVienRepository;
import com.example.be_datn.service.NhanVienService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NhanVienServiceImpl implements NhanVienService {
    private final NhanVienRepository nhanVienRepository;
    private final NhanVienEntity2Response mapper;

    @Override
    public Page<NhanVienResponse> gets(int page, int size) {
        var pageable = PageRequest.of(page, size);
        var response =  nhanVienRepository.findAll(pageable);
        return response.map(mapper::to);
    }

    @Transactional
    @Override
    public void create(NhanVienRequest request) {
        nhanVienRepository.findByTen(request.getTen()).orElseThrow(() -> new AppException(ErrorCode.NHAN_VIEN_EXIST));
        var nhanVien = mapper.toEntity(request);
        nhanVienRepository.save(nhanVien);
    }

    @Override
    public void delete(Long id) {
        nhanVienRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NHAN_VIEN_EXIST));
        nhanVienRepository.deleteById(id);
    }

    @Override
    public NhanVien get(Long id) {
        return nhanVienRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NHAN_VIEN_EXIST));
    }

    @Override
    public void update(NhanVienUpdate request, Long id) {
        var nhanVien = nhanVienRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NHAN_VIEN_EXIST));
        nhanVien = mapper.toEntity(request);
        nhanVienRepository.save(nhanVien);
    }
}