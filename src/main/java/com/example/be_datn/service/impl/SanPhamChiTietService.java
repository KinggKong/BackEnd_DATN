package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.SanPhamChiTietRequest;
import com.example.be_datn.dto.Response.SanPhamChiTietResponse;
import com.example.be_datn.entity.KichThuoc;
import com.example.be_datn.entity.MauSac;
import com.example.be_datn.entity.SanPham;
import com.example.be_datn.entity.SanPhamChiTiet;
import com.example.be_datn.repository.KichThuocRepository;
import com.example.be_datn.repository.MauSacRepository;
import com.example.be_datn.repository.SanPhamChiTietRepository;
import com.example.be_datn.service.ISanPhamChiTietService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SanPhamChiTietService implements ISanPhamChiTietService {
    SanPhamChiTietRepository sanPhamChiTietRepository;
    MauSacRepository mauSacRepository;
    KichThuocRepository kichThuocRepository;
    @Override
    public Page<SanPhamChiTietResponse> getAllPage(Pageable pageable) {
        return sanPhamChiTietRepository.findAll(pageable).map(SanPhamChiTietResponse::fromSanPhamChiTiet);
    }

    @Override
    public Page<SanPhamChiTietResponse> getAllPageBySanPhamId(Long id, Pageable pageable) {
        return sanPhamChiTietRepository.findBySanPhamId(id,pageable).map(SanPhamChiTietResponse::fromSanPhamChiTiet);
    }

    @Override
    public SanPhamChiTietResponse create(SanPhamChiTietRequest sanPhamChiTietRequest) {
        MauSac mauSac = mauSacRepository.findById(sanPhamChiTietRequest.getId_mauSac()).orElseThrow();
        KichThuoc kichThuoc = kichThuocRepository.findById(sanPhamChiTietRequest.getId_kichThuoc()).orElseThrow();
        SanPham sanPham = SanPham.builder().id(sanPhamChiTietRequest.getId_sanPham()).build();
        SanPhamChiTiet sanPhamChiTiet = SanPhamChiTiet.builder()
                .soLuong(sanPhamChiTietRequest.getSoLuong())
                .giaBan(sanPhamChiTietRequest.getGiaBan())
                .sanPham(sanPham)
                .kichThuoc(kichThuoc)
                .mauSac(mauSac)
                .trangThai(sanPhamChiTietRequest.getTrangThai())
                .build();
        SanPhamChiTiet savedSanPhamChiTiet = sanPhamChiTietRepository.save(sanPhamChiTiet);
        return SanPhamChiTietResponse.fromSanPhamChiTiet(savedSanPhamChiTiet);
    }

    @Override
    public SanPhamChiTietResponse update(Long id, SanPhamChiTietRequest sanPhamChiTietRequest) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(id).orElseThrow();
        MauSac mauSac = mauSacRepository.findById(sanPhamChiTietRequest.getId_mauSac()).orElseThrow();
        KichThuoc kichThuoc = kichThuocRepository.findById(sanPhamChiTietRequest.getId_kichThuoc()).orElseThrow();
        SanPham sanPham = SanPham.builder().id(sanPhamChiTietRequest.getId_sanPham()).build();
        sanPhamChiTiet.setSoLuong(sanPhamChiTietRequest.getSoLuong());
        sanPhamChiTiet.setGiaBan(sanPhamChiTietRequest.getGiaBan());
        sanPhamChiTiet.setSanPham(sanPham);
        sanPhamChiTiet.setKichThuoc(kichThuoc);
        sanPhamChiTiet.setMauSac(mauSac);
        sanPhamChiTiet.setTrangThai(sanPhamChiTietRequest.getTrangThai());
        SanPhamChiTiet savedSanPhamChiTiet = sanPhamChiTietRepository.save(sanPhamChiTiet);
        return SanPhamChiTietResponse.fromSanPhamChiTiet(savedSanPhamChiTiet);
    }

    @Override
    public SanPhamChiTietResponse getById(Long id) {
        return sanPhamChiTietRepository.findById(id).map(SanPhamChiTietResponse::fromSanPhamChiTiet).orElseThrow();
    }

    @Override
    public String delete(Long id) {

        sanPhamChiTietRepository.deleteById(id);
        return "deleted successfully";
    }
}
