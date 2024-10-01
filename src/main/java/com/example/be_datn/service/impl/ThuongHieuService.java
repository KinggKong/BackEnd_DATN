package com.example.be_datn.service.impl;

import com.example.be_datn.dto.request.ThuongHieuCreationRequest;
import com.example.be_datn.dto.request.ThuongHieuUpdateRequest;
import com.example.be_datn.dto.response.ThuongHieuResponse;
import com.example.be_datn.entity.ThuongHieu;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.mapper.ThuongHieuMapper;
import com.example.be_datn.repository.ThuongHieuRepository;
import com.example.be_datn.service.IThuongHieuService;
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
public class ThuongHieuService implements IThuongHieuService {
    ThuongHieuRepository thuongHieuRepository;
    private ThuongHieuMapper thuongHieuMapper;

    @Override
    public List<ThuongHieuResponse> getAllThuongHieu() {
        return thuongHieuMapper.toListThuongHieuResponse(thuongHieuRepository.findAll());
    }

    @Override
    public Page<ThuongHieu> getAllThuongHieuPageable(String tenThuongHieu, Pageable pageable) {
        return thuongHieuRepository.findThuongHieuByTenThuongHieuLike("%" + tenThuongHieu + "%", pageable);
    }

    @Override
    public ThuongHieuResponse createThuongHieu(ThuongHieuCreationRequest request) {
        if (thuongHieuRepository.existsThuongHieuByTenThuongHieu(request.getTenThuongHieu())) {
            throw new AppException(ErrorCode.THUONGHIEU_ALREADY_EXISTS);
        }
        ThuongHieu th= thuongHieuMapper.toThuongHieu(request);
        return thuongHieuMapper.toThuongHieuResponse(thuongHieuRepository.save(th));
    }

    @Override
    public ThuongHieuResponse getThuongHieuById(Long id) {
        return thuongHieuMapper.toThuongHieuResponse(thuongHieuRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.THUONGHIEU_NOT_FOUND)));
    }

    @Override
    public ThuongHieuResponse updateThuongHieu(Long idThuongHieu, ThuongHieuUpdateRequest request) {
        ThuongHieu thuongHieu= thuongHieuRepository.findById(idThuongHieu).orElse(null);
        thuongHieuMapper.updateThuongHieu(thuongHieu, request);
        return thuongHieuMapper.toThuongHieuResponse(thuongHieuRepository.save(thuongHieu));
    }

    @Override
    public String deleteThuongHieu(Long id) {
        getThuongHieuById(id);
        thuongHieuRepository.deleteById(id);
        return "deleted successfull";
    }

}