package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.DanhMucCreationRequest;
import com.example.be_datn.dto.Request.DanhMucUpdateRequest;
import com.example.be_datn.dto.response.DanhMucResponse;
import com.example.be_datn.entity.DanhMuc;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.mapper.DanhMucMapper;
import com.example.be_datn.repository.DanhMucRepository;
import com.example.be_datn.service.IDanhMucService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class DanhMucService implements IDanhMucService {
    DanhMucRepository danhMucRepository;
    private final DanhMucMapper danhMucMapper;

    @Override
    public List<DanhMucResponse> getAllDanhMuc() {
        return danhMucMapper.toListThuongHieuResponse( danhMucRepository.findAll());
    }

    @Override
    public Page<DanhMuc> getAllDanhMucPageable(String tenHang, Pageable pageable) {
        return danhMucRepository.findHangByHangLike("%" + tenHang + "%", pageable);
    }

    @Override
    public DanhMucResponse createDanhMuc(DanhMucCreationRequest request) {
        if (danhMucRepository.existsDanhMucByTenDanhMuc(request.getTenDanhMuc())) {
            throw new AppException(ErrorCode.HANG_ALREADY_EXISTS);
        }
        DanhMuc dm= danhMucMapper.toDanhMuc(request);
        return danhMucMapper.toDanhMucResponse(danhMucRepository.save(dm));
    }

    @Override
    public DanhMucResponse getDanhMucById(Long id) {
        return danhMucMapper.toDanhMucResponse(danhMucRepository.findById(id).get());
    }

    @Override
    public DanhMucResponse updateDanhMuc(Long idHang, DanhMucUpdateRequest request) {
        DanhMuc dm = danhMucRepository.findById(idHang).orElseThrow(null);
        danhMucMapper.updateDanhMuc(dm, request);
        return danhMucMapper.toDanhMucResponse(danhMucRepository.save(dm));
    }

    @Override
    public String deleteDanhMuc(Long id) {
        getDanhMucById(id);
        danhMucRepository.deleteById(id);
        return "deleted successfull";
    }

}