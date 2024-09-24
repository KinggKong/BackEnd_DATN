package com.example.be_datn.service;

import com.example.be_datn.entity.MauSac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
public interface IMauSacService {
    List<MauSac> getAllMauSac();

    Page<MauSac> getAllMauSac(Pageable pageable);

    MauSac createMauSac(MauSac mauSac);

    MauSac getMauSacById(Long id);

    MauSac updateMauSac(Long idMauSac, MauSac mauSac);

    String deleteMauSac(Long id);


}
