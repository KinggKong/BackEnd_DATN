package com.example.be_datn.service;

import com.example.be_datn.dto.Request.ThuongHieuCreationRequest;
import com.example.be_datn.dto.Request.ThuongHieuUpdateRequest;
import com.example.be_datn.dto.response.ThuongHieuResponse;
import com.example.be_datn.entity.ThuongHieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IThuongHieuService {
    List<ThuongHieuResponse> getAllThuongHieu();

    Page<ThuongHieu> getAllThuongHieuPageable(String tenThuongHieu,Pageable pageable);

    ThuongHieuResponse createThuongHieu(ThuongHieuCreationRequest request);

    ThuongHieuResponse getThuongHieuById(Long id);

    ThuongHieuResponse updateThuongHieu(Long idThuongHieu, ThuongHieuUpdateRequest request);

    String deleteThuongHieu(Long id);


}