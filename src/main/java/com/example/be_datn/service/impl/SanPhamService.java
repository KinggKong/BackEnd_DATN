package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.SanPhamRequest;
import com.example.be_datn.dto.Response.SanPhamResponse;
import com.example.be_datn.entity.SanPham;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.*;
import com.example.be_datn.service.ISanPhamService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SanPhamService implements ISanPhamService {
    SanPhamRepository sanPhamRepository;
    DanhMucRepository danhMucRepository;
    ThuongHieuRepository thuongHieuRepository;
    ChatLieuDeRepository chatLieuDeRepository;
    ChatLieuVaiRepository chatLieuVaiRepository;

    @Override
    public Page<SanPhamResponse> getAllPageable(Pageable pageable) {
        return sanPhamRepository.findAll(pageable).map(SanPhamResponse::fromSanPham);
    }

    @Override
    public List<SanPhamResponse> getAllByTenSanPhamContaning(String tenSanPham) {
        return sanPhamRepository.getAllByTenSanPhamContainingIgnoreCase(tenSanPham).stream().map(SanPhamResponse::fromSanPham).toList();
    }

    @Override
    public SanPhamResponse updateStatus(Long idSanPham, int status) {
        SanPham sanPham = sanPhamRepository.findById(idSanPham).orElseThrow(() -> new AppException(ErrorCode.SANPHAM_NOT_FOUND));
        sanPham.setTrangThai(status);
        return SanPhamResponse.fromSanPham(sanPhamRepository.save(sanPham));
    }

    @Override
    public Page<SanPhamResponse> getAllWithFilter(Long idDanhMuc, Long idThuongHieu, Long idChatLieuVai, Long idChatLieuDe, String tenSanPham, Pageable pageable) {
        return sanPhamRepository.getAllByFilter(idDanhMuc, idThuongHieu, idChatLieuVai, idChatLieuDe, "%" + tenSanPham + "%", pageable)
                .map(SanPhamResponse::fromSanPham);
    }

    @Override
    public SanPhamResponse getById(Long id) {
        return sanPhamRepository.findById(id).map(SanPhamResponse::fromSanPham).orElseThrow(() -> new AppException(ErrorCode.SANPHAM_NOT_FOUND));
    }

    @Override
    public SanPhamResponse create(SanPhamRequest sanPhamRequest) {
        if (sanPhamRepository.existsByTenSanPham(sanPhamRequest.getTenSanPham())) {
            throw new AppException(ErrorCode.TEN_SANPHAM_EXIST);
        }
        validateForeignKeys(sanPhamRequest);

        SanPham sanPham = buildSanPham(sanPhamRequest);
        return SanPhamResponse.fromSanPham(sanPhamRepository.save(sanPham));
    }

    @Override
    public SanPhamResponse update(SanPhamRequest sanPhamRequest, Long id) {
        SanPham sanPham = sanPhamRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SANPHAM_NOT_FOUND));

        validateForeignKeys(sanPhamRequest);

        updateEntityFromRequest(sanPham, sanPhamRequest);
        return SanPhamResponse.fromSanPham(sanPhamRepository.save(sanPham));
    }

    @Override
    public String delete(Long id) {
        if (sanPhamRepository.existsById(id)) {
            sanPhamRepository.deleteById(id);
            return "Deleted successfully";
        } else {
            throw new AppException(ErrorCode.SANPHAM_NOT_FOUND);
        }

    }


    private void validateForeignKeys(SanPhamRequest request) {
        if (!chatLieuDeRepository.existsById(request.getIdChatLieuDe())) {
            throw new AppException(ErrorCode.CHATLIEUDE_NOT_FOUND);
        }
        if (!danhMucRepository.existsById(request.getIdDanhMuc())) {
            throw new AppException(ErrorCode.DANHMUC_NOT_FOUND);
        }
        if (!thuongHieuRepository.existsById(request.getIdThuongHieu())) {
            throw new AppException(ErrorCode.THUONGHIEU_NOT_FOUND);
        }
        if (!chatLieuVaiRepository.existsById(request.getIdChatLieuVai())) {
            throw new AppException(ErrorCode.CHATLIEUVAI_NOT_FOUND);
        }
    }

    private void updateEntityFromRequest(SanPham sanPham, SanPhamRequest request) {
        sanPham.setTenSanPham(request.getTenSanPham())
                .setMoTa(request.getMoTa())
                .setTrangThai(request.getTrangThai())
                .setDanhMuc(danhMucRepository.findById(request.getIdDanhMuc())
                        .orElseThrow(() -> new AppException(ErrorCode.DANHMUC_NOT_FOUND)))
                .setChatLieuDe(chatLieuDeRepository.findById(request.getIdChatLieuDe())
                        .orElseThrow(() -> new AppException(ErrorCode.CHATLIEUDE_NOT_FOUND)))
                .setChatLieuVai(chatLieuVaiRepository.findById(request.getIdChatLieuVai())
                        .orElseThrow(() -> new AppException(ErrorCode.CHATLIEUVAI_NOT_FOUND)))
                .setThuongHieu(thuongHieuRepository.findById(request.getIdThuongHieu())
                        .orElseThrow(() -> new AppException(ErrorCode.THUONGHIEU_NOT_FOUND)));
    }

    private SanPham buildSanPham(SanPhamRequest request) {
        return SanPham.builder()
                .tenSanPham(request.getTenSanPham())
                .moTa(request.getMoTa())
                .chatLieuVai(chatLieuVaiRepository.findById(request.getIdChatLieuVai()).orElseThrow(() -> new AppException(ErrorCode.CHATLIEUVAI_NOT_FOUND)))
                .chatLieuDe(chatLieuDeRepository.findById(request.getIdChatLieuDe()).orElseThrow(() -> new AppException(ErrorCode.CHATLIEUDE_NOT_FOUND)))
                .thuongHieu(thuongHieuRepository.findById(request.getIdThuongHieu()).orElseThrow(() -> new AppException(ErrorCode.THUONGHIEU_NOT_FOUND)))
                .danhMuc(danhMucRepository.findById(request.getIdDanhMuc()).orElseThrow(() -> new AppException(ErrorCode.DANHMUC_NOT_FOUND)))
                .trangThai(request.getTrangThai())
                .build();
    }
}
