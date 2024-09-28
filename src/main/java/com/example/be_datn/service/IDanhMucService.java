package com.example.be_datn.service;

import com.example.be_datn.entity.DanhMuc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDanhMucService {
    List<DanhMuc> getAllDanhMuc();

    Page<DanhMuc> getAllDanhMucPageable(String tenDanhMuc, Pageable pageable);

    DanhMuc createDanhMuc(DanhMuc DanhMuc);

    DanhMuc getDanhMucById(Long id);

    DanhMuc updateDanhMuc(Long idDanhMuc, DanhMuc DanhMuc);

    String deleteDanhMuc(Long id);


}