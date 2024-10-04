package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.SanPhamChiTietRequest;
import com.example.be_datn.dto.Response.SanPhamChiTietResponse;
import com.example.be_datn.entity.*;
import com.example.be_datn.repository.KichThuocRepository;
import com.example.be_datn.repository.MauSacRepository;
import com.example.be_datn.repository.SanPhamChiTietRepository;
import com.example.be_datn.service.ISanPhamChiTietService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SanPhamChiTietService implements ISanPhamChiTietService {
    SanPhamChiTietRepository sanPhamChiTietRepository;
    MauSacRepository mauSacRepository;
    KichThuocRepository kichThuocRepository;
    HinhAnhService hinhAnhService;
    @Override
    public Page<SanPhamChiTietResponse> getAllPage(Pageable pageable) {
        return sanPhamChiTietRepository.findAll(pageable).map(SanPhamChiTietResponse::fromSanPhamChiTiet);
    }

    @Override
    public Page<SanPhamChiTietResponse> getAllPageBySanPhamId(Long id, Pageable pageable) {
        return sanPhamChiTietRepository.findBySanPhamId(id,pageable).map(SanPhamChiTietResponse::fromSanPhamChiTiet);
    }

// @Override
//    public List<SanPhamChiTietResponse> create(List<SanPhamChiTietRequest> sanPhamChiTietRequest) {
//        List<SanPhamChiTietResponse> sanPhamChiTietResponses = new ArrayList<>();
//        for (SanPhamChiTietRequest sanPhamChiTietRequest1 : sanPhamChiTietRequest) {
//            MauSac mauSac = mauSacRepository.findById(sanPhamChiTietRequest1.getId_mauSac()).orElseThrow();
//            KichThuoc kichThuoc = kichThuocRepository.findById(sanPhamChiTietRequest1.getId_kichThuoc()).orElseThrow();
//            SanPham sanPham = SanPham.builder().id(sanPhamChiTietRequest1.getId_sanPham()).build();
////            for (HinhAnh hinhAnh : sanPhamChiTietRequest1.getHinhAnh()) {
////                hinhAnh.setSanPhamChiTiet(SanPhamChiTiet.builder().maSanPham(sanPhamChiTietRequest1.getMaSanPham()).build());
////            }
//            SanPhamChiTiet sanPhamChiTiet = SanPhamChiTiet.builder()
//                    .soLuong(sanPhamChiTietRequest1.getSoLuong())
//                    .giaBan(sanPhamChiTietRequest1.getGiaBan())
//                    .sanPham(sanPham)
//                    .kichThuoc(kichThuoc)
//                    .mauSac(mauSac)
//                    .maSanPham(sanPhamChiTietRequest1.getMaSanPham())
//                    .trangThai(sanPhamChiTietRequest1.getTrangThai())
//                    .build();
//            sanPhamChiTietRepository.save(sanPhamChiTiet);
//            sanPhamChiTietResponses.add(SanPhamChiTietResponse.fromSanPhamChiTiet(sanPhamChiTiet));
//        }
//       return sanPhamChiTietResponses;
//    }
    @Override
    public List<SanPhamChiTietResponse> create(List<SanPhamChiTietRequest> sanPhamChiTietRequest) throws IOException {
        List<SanPhamChiTietResponse> sanPhamChiTietResponses = new ArrayList<>();

        // Dịch vụ Firebase Storage


        for (SanPhamChiTietRequest sanPhamChiTietRequest1 : sanPhamChiTietRequest) {
            MauSac mauSac = mauSacRepository.findById(sanPhamChiTietRequest1.getId_mauSac()).orElseThrow();
            KichThuoc kichThuoc = kichThuocRepository.findById(sanPhamChiTietRequest1.getId_kichThuoc()).orElseThrow();
            SanPham sanPham = SanPham.builder().id(sanPhamChiTietRequest1.getId_sanPham()).build();



            SanPhamChiTiet sanPhamChiTiet = SanPhamChiTiet.builder()
                    .soLuong(sanPhamChiTietRequest1.getSoLuong())
                    .giaBan(sanPhamChiTietRequest1.getGiaBan())
                    .sanPham(sanPham)
                    .kichThuoc(kichThuoc)
                    .mauSac(mauSac)
                    .maSanPham(sanPhamChiTietRequest1.getMaSanPham())
                    .trangThai(sanPhamChiTietRequest1.getTrangThai())
//                    .hinhAnhList(danhSachHinhAnh) // Gán danh sách hình ảnh
                    .build();


            sanPhamChiTiet = sanPhamChiTietRepository.save(sanPhamChiTiet);
            for (String src: sanPhamChiTietRequest1.getHinhanh()) {
                hinhAnhService.addHinhAnh(src,sanPhamChiTiet.getId(), sanPhamChiTiet);
            }
            sanPhamChiTietResponses.add(SanPhamChiTietResponse.fromSanPhamChiTiet(sanPhamChiTiet));
        }
        return sanPhamChiTietResponses;
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
