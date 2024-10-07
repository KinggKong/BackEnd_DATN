package com.example.be_datn.service;

import com.example.be_datn.dto.Request.diaChi.DiaChiCreationRequest;
import com.example.be_datn.dto.Request.diaChi.DiaChiUpdateRequest;
import com.example.be_datn.dto.Response.DiaChiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDiaChiService {
    public DiaChiResponse createDiaChi(DiaChiCreationRequest request);
    public DiaChiResponse getDiaChiById(Long id);
    public Page<DiaChiResponse> getDiaChiList(String tenDiaChi, Pageable pageable);
    public DiaChiResponse updateDiaChi(Long id, DiaChiUpdateRequest request);
    public List<DiaChiResponse> getDiaChiList();
    public void deleteDiaChiById(Long id);
}
