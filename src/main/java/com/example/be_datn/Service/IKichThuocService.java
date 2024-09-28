package com.example.be_datn.Service;

import com.example.be_datn.Entity.KichThuoc; // Đảm bảo import KichThuoc
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IKichThuocService {
    List<KichThuoc> getAllKichThuoc();

    Page<KichThuoc> getAllKichThuocPageable(String tenKichThuoc, Pageable pageable);
    KichThuoc createKichThuoc(KichThuoc kichThuoc);

    KichThuoc getKichThuocById(Long id);

    KichThuoc updateKichThuoc(Long idKichThuoc, KichThuoc kichThuoc);

    String deleteKichThuoc(Long id);
}
