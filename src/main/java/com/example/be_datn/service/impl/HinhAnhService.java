package com.example.be_datn.service.impl;

import com.example.be_datn.entity.HinhAnh;
import com.example.be_datn.entity.SanPhamChiTiet;
import com.example.be_datn.repository.HinhAnhRepository;
import com.example.be_datn.service.IHinhAnhService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<HinhAnh> getAll() {
        return hinhAnhRepository.findAll();
    }

    @Override
    public List<HinhAnh> getAllByIdSanPhamCt(Long idSanPhamCt) {
        return hinhAnhRepository.findByIdSanPhamCt(idSanPhamCt);
    }

    @Override
    public void deleteHinhAnhByUrl(String url) {
        HinhAnh hinhAnh = hinhAnhRepository.findByUrl(url);
        hinhAnhRepository.delete(hinhAnh);
    }

    public HinhAnh findBySanPhamChiTietId(String src) {
        return hinhAnhRepository.findBySanPhamChiTietId(src);
    }
}
