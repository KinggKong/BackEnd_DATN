package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.SanPhamChiTietRequest;
import com.example.be_datn.dto.Response.SanPhamChiTietResponse;
import com.example.be_datn.entity.*;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.KichThuocRepository;
import com.example.be_datn.repository.MauSacRepository;
import com.example.be_datn.repository.SanPhamChiTietRepository;
import com.example.be_datn.repository.SanPhamRepository;
import com.example.be_datn.service.ISanPhamChiTietService;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SanPhamChiTietService implements ISanPhamChiTietService {
    SanPhamChiTietRepository sanPhamChiTietRepository;
    MauSacRepository mauSacRepository;
    KichThuocRepository kichThuocRepository;
    HinhAnhService hinhAnhService;
    SanPhamRepository sanPhamRepository;
    @Override
    public Page<SanPhamChiTietResponse> getAllPage(Pageable pageable) {
        return sanPhamChiTietRepository.findAll(pageable).map(SanPhamChiTietResponse::fromSanPhamChiTiet);
    }

    @Override
    public Page<SanPhamChiTietResponse> getAllPageBySanPhamId(Long id, Pageable pageable) {
        return sanPhamChiTietRepository.findSPCTBySanPhamId(id,pageable).map(SanPhamChiTietResponse::fromSanPhamChiTiet);
    }

    @Override
    public Page<SanPhamChiTietResponse> getAllByFilter(Long idDanhMuc, Long idThuongHieu, Long idChatLieuVai, Long idChatLieuDe, Long idSanPham, Pageable pageable) {
        return sanPhamChiTietRepository.getAllByFilter(idDanhMuc, idThuongHieu, idChatLieuVai, idChatLieuDe, idSanPham, pageable)
                .map(SanPhamChiTietResponse::fromSanPhamChiTiet);
    }


    @Override
    public List<SanPhamChiTietResponse> create(List<SanPhamChiTietRequest> sanPhamChiTietRequest) throws IOException {
        List<SanPhamChiTietResponse> sanPhamChiTietResponses = new ArrayList<>();




        for (SanPhamChiTietRequest sanPhamChiTietRequest1 : sanPhamChiTietRequest) {
            if(sanPhamChiTietRequest1.getGiaBan()<0){
                throw new AppException(ErrorCode.GIA_BAN_INVALID);
            }
            if(sanPhamChiTietRequest1.getSoLuong()<0){
                throw new AppException(ErrorCode.SO_LUONG_INVALID);
            }
            MauSac mauSac = mauSacRepository.findById(sanPhamChiTietRequest1.getId_mauSac()).orElseThrow(() -> new AppException(ErrorCode.MAUSAC_NOT_FOUND));
            KichThuoc kichThuoc = kichThuocRepository.findById(sanPhamChiTietRequest1.getId_kichThuoc()).orElseThrow(() -> new AppException(ErrorCode.KICHTHUOC_NOT_FOUND));
            SanPham sanPham = sanPhamRepository.findById(sanPhamChiTietRequest1.getId_sanPham()).orElseThrow(()-> new AppException(ErrorCode.SANPHAM_NOT_FOUND));
            SanPhamChiTiet sanPhamChiTietCheck = sanPhamChiTietRepository.findBySanPhamIdAndMauSacIdAndKichThuocId(sanPham.getId(),mauSac.getId(),kichThuoc.getId());
            if (sanPhamChiTietCheck != null) {
                sanPhamChiTietCheck.setSoLuong(sanPhamChiTietCheck.getSoLuong() + sanPhamChiTietRequest1.getSoLuong());
                sanPhamChiTietCheck.setGiaBan(sanPhamChiTietRequest1.getGiaBan());
                sanPhamChiTietCheck.setTrangThai(sanPhamChiTietRequest1.getTrangThai());
                sanPhamChiTietCheck = sanPhamChiTietRepository.save(sanPhamChiTietCheck);
                List<String> existingImages = hinhAnhService.getAllByIdSanPhamCt(sanPhamChiTietCheck.getId())
                        .stream().map(HinhAnh::getUrl).collect(Collectors.toList());

                // Ảnh mới từ request
                List<String> newImages = sanPhamChiTietRequest1.getHinhAnh();

                // Thêm các ảnh mới (nếu chưa tồn tại)
                for (String src : newImages) {
                    if (!existingImages.contains(src)) {
                        hinhAnhService.addHinhAnh(src, sanPhamChiTietCheck.getId(), sanPhamChiTietCheck);
                    }
                }

                // Xóa các ảnh không còn trong request nhưng có trong database
                for (String existingImage : existingImages) {
                    if (!newImages.contains(existingImage)) {
                        hinhAnhService.deleteHinhAnhByUrl(existingImage);
                    }
                }
                sanPhamChiTietResponses.add(SanPhamChiTietResponse.fromSanPhamChiTiet(sanPhamChiTietCheck));
                continue;
            }


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
            for (String src: sanPhamChiTietRequest1.getHinhAnh()) {
                hinhAnhService.addHinhAnh(src,sanPhamChiTiet.getId(), sanPhamChiTiet);
            }
            sanPhamChiTietResponses.add(SanPhamChiTietResponse.fromSanPhamChiTiet(sanPhamChiTiet));
        }
        return sanPhamChiTietResponses;
    }
    @Override
    public SanPhamChiTietResponse update(Long id, SanPhamChiTietRequest sanPhamChiTietRequest) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SANPHAM_NOT_FOUND));
        if(sanPhamChiTietRequest.getGiaBan()<0){
            throw new AppException(ErrorCode.GIA_BAN_INVALID);
        }
        if(sanPhamChiTietRequest.getSoLuong()<0){
            throw new AppException(ErrorCode.SO_LUONG_INVALID);
        }
        MauSac mauSac = mauSacRepository.findById(sanPhamChiTietRequest.getId_mauSac()).orElseThrow(() -> new AppException(ErrorCode.MAUSAC_NOT_FOUND));
        KichThuoc kichThuoc = kichThuocRepository.findById(sanPhamChiTietRequest.getId_kichThuoc()).orElseThrow(() -> new AppException(ErrorCode.KICHTHUOC_NOT_FOUND));
        SanPham sanPham = sanPhamRepository.findById(sanPhamChiTietRequest.getId_sanPham()).orElseThrow(()-> new AppException(ErrorCode.SANPHAM_NOT_FOUND));
        sanPhamChiTiet.setSoLuong(sanPhamChiTietRequest.getSoLuong());
        sanPhamChiTiet.setGiaBan(sanPhamChiTietRequest.getGiaBan());
        sanPhamChiTiet.setSanPham(sanPham);
        sanPhamChiTiet.setKichThuoc(kichThuoc);
        sanPhamChiTiet.setMauSac(mauSac);
        sanPhamChiTiet.setTrangThai(sanPhamChiTietRequest.getTrangThai());
        SanPhamChiTiet savedSanPhamChiTiet = sanPhamChiTietRepository.save(sanPhamChiTiet);

        List<String> existingImages = hinhAnhService.getAllByIdSanPhamCt(sanPhamChiTiet.getId())
                .stream().map(HinhAnh::getUrl).collect(Collectors.toList());

        // Ảnh mới từ request
        List<String> newImages = sanPhamChiTietRequest.getHinhAnh();

        // Thêm các ảnh mới (nếu chưa tồn tại)
        for (String src : newImages) {
            if (!existingImages.contains(src)) {
                hinhAnhService.addHinhAnh(src, sanPhamChiTiet.getId(), sanPhamChiTiet);
            }
        }

        // Xóa các ảnh không còn trong request nhưng có trong database
        for (String existingImage : existingImages) {
            if (!newImages.contains(existingImage)) {
                hinhAnhService.deleteHinhAnhByUrl(existingImage);
            }
        }
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

    @Override
    public SanPhamChiTietResponse updateStatus(Long idSanPhamChiTiet, int status) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(idSanPhamChiTiet).orElseThrow(() -> new AppException(ErrorCode.SANPHAM_NOT_FOUND));
        sanPhamChiTiet.setTrangThai(status);
        return SanPhamChiTietResponse.fromSanPhamChiTiet(sanPhamChiTietRepository.save(sanPhamChiTiet));
    }

    @Override
    public SanPhamChiTietResponse getSPCTByMauSacAndKichThuoc(Long idSanPham,Long idMauSac, Long idKichThuoc) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findBySanPhamIdAndMauSacIdAndKichThuocId(idSanPham,idMauSac,idKichThuoc);
        if (sanPhamChiTiet == null) {
            // Ném ngoại lệ hoặc trả về giá trị mặc định
            throw new EntityNotFoundException("Không tìm thấy sản phẩm chi tiết với ID sản phẩm: " + idSanPham +
                    ", ID màu sắc: " + idMauSac +
                    ", ID kích thước: " + idKichThuoc);
        }
        return SanPhamChiTietResponse.fromSanPhamChiTiet(sanPhamChiTiet);
    }
}
