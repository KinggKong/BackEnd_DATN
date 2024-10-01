package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.KichThuocRequest;
import com.example.be_datn.dto.Response.KichThuocResponse;
import com.example.be_datn.entity.KichThuoc;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.KichThuocRepository;
import com.example.be_datn.service.IKichThuocService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class KichThuocService implements IKichThuocService {
    KichThuocRepository kichThuocRepository;

    @Override
    public Page<KichThuocResponse> getAllKichThuocPageable(String tenKichThuoc, Pageable pageable) {
        return kichThuocRepository.findKichThuocByTenKichThuocLike("%" + tenKichThuoc + "%", pageable)
                .map(KichThuocResponse::fromKichThuoc);
    }

    @Override
    public KichThuocResponse createKichThuoc(KichThuocRequest kichThuocRequest) {
        if (kichThuocRepository.existsKichThuocByTenKichThuoc(kichThuocRequest.getTenKichThuoc())) {
            throw new AppException(ErrorCode.KICHTHUOC_ALREADY_EXISTS);
        }
        KichThuoc kichThuoc = KichThuoc.builder()
                .tenKichThuoc(kichThuocRequest.getTenKichThuoc())
                .trangThai(kichThuocRequest.getTrangThai())
                .build();
        KichThuoc savedKichThuoc = kichThuocRepository.save(kichThuoc);
        return KichThuocResponse.fromKichThuoc(savedKichThuoc);
    }

    @Override
    public KichThuocResponse getKichThuocById(Long id) {
        return kichThuocRepository.findById(id)
                .map(KichThuocResponse::fromKichThuoc)
                .orElseThrow(() -> new AppException(ErrorCode.KICHTHUOC_NOT_FOUND));
    }

    @Override
    public KichThuocResponse updateKichThuoc(Long idKichThuoc, KichThuocRequest kichThuocRequest) {
        KichThuoc kichThuoc = kichThuocRepository.findById(idKichThuoc)
                .orElseThrow(() -> new AppException(ErrorCode.KICHTHUOC_NOT_FOUND));
        kichThuoc.setTenKichThuoc(kichThuocRequest.getTenKichThuoc());
        kichThuoc.setTrangThai(kichThuocRequest.getTrangThai());
        return KichThuocResponse.fromKichThuoc(kichThuocRepository.save(kichThuoc));
    }

    @Override
    public String deleteKichThuoc(Long id) {
        getKichThuocById(id);
        kichThuocRepository.deleteById(id);
        return "deleted successfully";
    }
}
