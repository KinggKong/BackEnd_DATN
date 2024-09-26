package com.example.be_datn.Service.impl;

import com.example.be_datn.Entity.DanhMuc;
import com.example.be_datn.Exception.AppException;
import com.example.be_datn.Exception.ErrorCode;
import com.example.be_datn.Repository.DanhMucRepository;
import com.example.be_datn.Service.IDanhMucService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class DanhMucService implements IDanhMucService {
    DanhMucRepository danhMucRepository;

    @Override
    public List<DanhMuc> getAllDanhMuc() {
        return danhMucRepository.findAll();
    }

    @Override
    public Page<DanhMuc> getAllDanhMucPageable(String tenHang, Pageable pageable) {
        return danhMucRepository.findHangByHangLike("%" + tenHang + "%", pageable);
    }

    @Override
    public DanhMuc createDanhMuc(DanhMuc danhMuc) {
        if (danhMucRepository.existsDanhMucByTenDanhMuc(danhMuc.getTenDanhMuc())) {
            throw new AppException(ErrorCode.HANG_ALREADY_EXISTS);
        }
        return danhMucRepository.save(danhMuc);
    }

    @Override
    public DanhMuc getDanhMucById(Long id) {
        return danhMucRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.HANG_NOT_FOUND));
    }

    @Override
    public DanhMuc updateDanhMuc(Long idHang, DanhMuc danhMuc) {
        Optional<DanhMuc> hangOptional = danhMucRepository.findById(idHang);
        if (hangOptional.isPresent()) {
            DanhMuc danhMucUpdate = hangOptional.get();
            danhMucUpdate.setTenDanhMuc(danhMuc.getTenDanhMuc());
            danhMucUpdate.setTrangThai(danhMuc.getTrangThai());
            danhMucUpdate.setUpdatedAt(danhMuc.getUpdatedAt());
            return danhMucRepository.save(danhMucUpdate);
        } else {
            throw new AppException(ErrorCode.HANG_NOT_FOUND);
        }
    }

    @Override
    public String deleteDanhMuc(Long id) {
        getDanhMucById(id);
        danhMucRepository.deleteById(id);
        return "deleted successfull";
    }

}
