package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.KhachHangRequest1;
import com.example.be_datn.dto.Response.KhachHangResponse1;
import com.example.be_datn.entity.KhachHang;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.KhachHangRepository;
import com.example.be_datn.service.IKhachHangService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class KhachHangService implements IKhachHangService {

    KhachHangRepository khachHangRepository;

    @Override
    public Page<KhachHangResponse1> getAllKhachHangPageable(String ten, Pageable pageable) {
        return khachHangRepository.findKhachHangByTenLike("%" + ten + "%", pageable)
                .map(KhachHangResponse1::fromKhachHang);
    }

    @Override
    public KhachHangResponse1 createKhachHang(KhachHangRequest1 khachHangRequest) {
        // Kiểm tra xem có khách hàng với email hoặc số điện thoại đã tồn tại không
        if (khachHangRepository.existsKhachHangByEmail(khachHangRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (khachHangRepository.existsKhachHangBySdt(khachHangRequest.getSdt())) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);
        }

        // Tạo khách hàng mới từ dữ liệu request
        KhachHang khachHang = KhachHang.builder()
                .ten(khachHangRequest.getTen())
                .ma(khachHangRequest.getMa())
                .email(khachHangRequest.getEmail())
                .sdt(khachHangRequest.getSdt())
                .avatar(khachHangRequest.getAvatar())
                .ngaySinh(khachHangRequest.getNgaySinh())
                .gioiTinh(khachHangRequest.getGioiTinh())
                .diaChi(khachHangRequest.getDiaChiStr())
                .trangThai(khachHangRequest.getTrangThai())
                .build();

        // Lưu khách hàng vào cơ sở dữ liệu
        KhachHang savedKhachHang = khachHangRepository.save(khachHang);
        return KhachHangResponse1.fromKhachHang(savedKhachHang);
    }

    @Override
    public KhachHangResponse1 getKhachHangById(Long id) {
        // Tìm khách hàng theo ID
        return khachHangRepository.findById(id)
                .map(KhachHangResponse1::fromKhachHang)
                .orElseThrow(() -> new AppException(ErrorCode.KHACH_HANG_NOT_FOUND));
    }

    @Override
    public KhachHangResponse1 updateKhachHang(Long id, KhachHangRequest1 khachHangRequest) {
        // Tìm khách hàng theo ID
        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.KHACH_HANG_NOT_FOUND));

        // Cập nhật thông tin khách hàng
        khachHang.setTen(khachHangRequest.getTen());
        khachHang.setMa(khachHangRequest.getMa());
        khachHang.setEmail(khachHangRequest.getEmail());
        khachHang.setSdt(khachHangRequest.getSdt());
        khachHang.setAvatar(khachHangRequest.getAvatar());
        khachHang.setNgaySinh(khachHangRequest.getNgaySinh());
        khachHang.setDiaChi(khachHangRequest.getDiaChiStr());
        khachHang.setGioiTinh(khachHangRequest.getGioiTinh());
        khachHang.setTrangThai(khachHangRequest.getTrangThai());

        // Lưu cập nhật vào cơ sở dữ liệu
        return KhachHangResponse1.fromKhachHang(khachHangRepository.save(khachHang));
    }

    @Override
    public String deleteKhachHang(Long id) {
        // Kiểm tra sự tồn tại của khách hàng trước khi xóa
        getKhachHangById(id);
        khachHangRepository.deleteById(id);
        return "deleted successfully";
    }
}
