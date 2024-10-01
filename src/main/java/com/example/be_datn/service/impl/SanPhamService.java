package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.SanPhamRequest;
import com.example.be_datn.dto.Response.SanPhamResponse;
import com.example.be_datn.entity.ChatLieuVai;
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
    public SanPhamResponse getById(Long id) {
        return sanPhamRepository.findById(id).map(SanPhamResponse::fromSanPham).orElseThrow(() -> new ArithmeticException(ErrorCode.SANPHAM_NOT_FOUND.getMessage()));
    }

    @Override
    public SanPhamResponse create(SanPhamRequest sanPhamRequest) {
        if (sanPhamRepository.existsByTenSanPham(sanPhamRequest.getTenSanPham())) {
            throw new AppException(ErrorCode.TEN_SANPHAM_EXIST);
        }
        // chưa test trường hợp chất liệu vải không có trong database vì chưa có code
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
        var sanPham = sanPhamRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SANPHAM_NOT_FOUND));
        sanPhamRepository.deleteById(id);
        return "Deleted successfully";
    }


    private void validateForeignKeys(SanPhamRequest request) {
        chatLieuDeRepository.findById(request.getIdChatLieuDe())
                .orElseThrow(() -> new AppException(ErrorCode.CHATLIEUDE_NOT_FOUND));

        danhMucRepository.findById(request.getIdDanhMuc())
                .orElseThrow(() -> new AppException(ErrorCode.DANHMUC_NOT_FOUND));

        thuongHieuRepository.findById(request.getIdThuongHieu())
                .orElseThrow(() -> new AppException(ErrorCode.THUONGHIEU_NOT_FOUND));
        chatLieuVaiRepository.findById(request.getIdChatLieuVai()).orElseThrow(() -> new AppException(ErrorCode.CHATLIEUVAI_NOT_FOUND));
    }

    private void updateEntityFromRequest(SanPham sanPham, SanPhamRequest request) {
        sanPham.setTenSanPham(request.getTenSanPham())
                .setMoTa(request.getMoTa())
                .setTrangThai(request.getTrangThai())
                .setDanhMuc(danhMucRepository.findById(request.getIdDanhMuc()).get())
                .setChatLieuDe(chatLieuDeRepository.findById(request.getIdChatLieuDe()).get())
                .setChatLieuVai(chatLieuVaiRepository.findById(request.getIdChatLieuVai()).get())
                .setThuongHieu(thuongHieuRepository.findById(request.getIdThuongHieu()).get());
    }

    private SanPham buildSanPham(SanPhamRequest request) {
        return SanPham.builder()
                .tenSanPham(request.getTenSanPham())
                .moTa(request.getMoTa())
                .ChatLieuVai(chatLieuVaiRepository.findById(request.getIdChatLieuVai()).get())
                .ChatLieuDe(chatLieuDeRepository.findById(request.getIdChatLieuDe()).get())
                .ThuongHieu(thuongHieuRepository.findById(request.getIdThuongHieu()).get())
                .trangThai(request.getTrangThai())
                .DanhMuc(danhMucRepository.findById(request.getIdDanhMuc()).get())
                .build();
    }
}
