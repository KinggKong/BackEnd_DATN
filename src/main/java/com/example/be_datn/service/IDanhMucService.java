package com.example.be_datn.service;

import com.example.be_datn.dto.Request.DanhMucCreationRequest;
import com.example.be_datn.dto.Request.DanhMucUpdateRequest;
import com.example.be_datn.dto.Response.DanhMucResponse;
import com.example.be_datn.entity.DanhMuc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDanhMucService {
    List<DanhMucResponse> getAllDanhMuc();

    Page<DanhMuc> getAllDanhMucPageable(String tenDanhMuc, Pageable pageable);

    DanhMucResponse createDanhMuc(DanhMucCreationRequest request);

    DanhMucResponse getDanhMucById(Long id);

    DanhMucResponse updateDanhMuc(Long idDanhMuc, DanhMucUpdateRequest request);

    String deleteDanhMuc(Long id);


}