package com.example.be_datn.service.impl;

import com.example.be_datn.entity.HinhAnh;
import com.example.be_datn.entity.SanPhamChiTiet;
import com.example.be_datn.repository.HinhAnhRepository;
import com.example.be_datn.service.IHinhAnhService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class HinhAnhService implements IHinhAnhService {
    HinhAnhRepository hinhAnhRepository;
    @Override
    public void addHinhAnh(String url, Long idSanPhamCt, SanPhamChiTiet sanPhamChiTiet) {
        HinhAnh hinhAnh = new HinhAnh();
        hinhAnh.setUrl(url);
        hinhAnh.setTrangThai(1);
        hinhAnh.setIdSanPhamCt(idSanPhamCt);
        hinhAnh.setSanPhamChiTiet(sanPhamChiTiet);
        hinhAnhRepository.save(hinhAnh);
    }
}
