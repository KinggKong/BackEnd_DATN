package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.diaChi.DiaChiCreationRequest;
import com.example.be_datn.dto.Request.diaChi.DiaChiUpdateRequest;
import com.example.be_datn.dto.Response.DiaChiResponse;
import com.example.be_datn.entity.DiaChi;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.mapper.DiaChiMapper;
import com.example.be_datn.repository.DiaChiRepository;
import com.example.be_datn.service.IDiaChiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DiaChiService implements IDiaChiService {
    private final DiaChiRepository diaChiRepository;
    private final DiaChiMapper diaChiMapper;

    @Override
    public DiaChiResponse createDiaChi(DiaChiCreationRequest request) {
        DiaChi diaChi= diaChiMapper.toDiaChi(request);
        return diaChiMapper.toDiaChiResponse(diaChiRepository.save(diaChi));
    }

    @Override
    public DiaChiResponse getDiaChiById(Long id) {
        DiaChi diaChi= diaChiRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.DIA_CHI_NOT_FOUND));
        return diaChiMapper.toDiaChiResponse(diaChi);
    }

    @Override
    public Page<DiaChiResponse> getDiaChiList(String tenDiaChi, Pageable pageable) {
        return null;
    }

    @Override
    public DiaChiResponse updateDiaChi(Long id, DiaChiUpdateRequest request) {
        DiaChi diaChi= diaChiRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.DIA_CHI_NOT_FOUND));
        diaChiMapper.updateDiaChi(diaChi, request);
        return diaChiMapper.toDiaChiResponse(diaChiRepository.save(diaChi));
    }

    @Override
    public List<DiaChiResponse> getDiaChiList() {
        return diaChiMapper.toListDiaChiResponse(diaChiRepository.findAll());
    }

    @Override
    public void deleteDiaChiById(Long id) {
        DiaChi diaChi= diaChiRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.DIA_CHI_NOT_FOUND));
    }
}
