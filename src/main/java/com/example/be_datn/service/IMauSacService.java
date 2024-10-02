package com.example.be_datn.service;

import com.example.be_datn.dto.Request.MauSacRequest;
import com.example.be_datn.dto.Response.MauSacResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMauSacService {

    Page<MauSacResponse> getAllMauSacPageable(String ten_Mau, Pageable pageable);

    MauSacResponse createMauSac(MauSacRequest mauSacRequest);

    MauSacResponse getMauSacById(Long id);

    MauSacResponse updateMauSac(Long idMauSac, MauSacRequest mauSacRequest);

    String deleteMauSac(Long id);


}
