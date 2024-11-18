package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.NhanVienRequest;
import com.example.be_datn.dto.Response.NhanVienResponse;
import com.example.be_datn.entity.NhanVien;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.NhanVienRepository;
import com.example.be_datn.service.INhanVienService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class NhanVienService implements INhanVienService {

    NhanVienRepository nhanVienRepository;

    @Override
    public Page<NhanVienResponse> getAllNhanVienPageable(String ten, Pageable pageable) {
        // Lọc nhân viên theo tên và phân trang
        return nhanVienRepository.findNhanVienByTenLike("%" + ten + "%", pageable)
                .map(NhanVienResponse::fromNhanVien);
    }

    @Override
    public NhanVienResponse createNhanVien(NhanVienRequest nhanVienRequest) {
        // Kiểm tra xem có nhân viên với email hoặc số điện thoại đã tồn tại không
        if (nhanVienRepository.existsNhanVienByEmail(nhanVienRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (nhanVienRepository.existsNhanVienBySdt(nhanVienRequest.getSdt())) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);
        }

        // Tạo nhân viên mới từ dữ liệu request
        NhanVien nhanVien = NhanVien.builder()
                .ten(nhanVienRequest.getTen())
                .email(nhanVienRequest.getEmail())
                .sdt(nhanVienRequest.getSdt())
                .avatar(nhanVienRequest.getAvatar())
                .ngaySinh(nhanVienRequest.getNgaySinh())
                .diaChi(nhanVienRequest.getDiaChi())
                .idTaiKhoan(nhanVienRequest.getIdTaiKhoan())
                .gioiTinh(nhanVienRequest.getGioiTinh())
                .trangThai(nhanVienRequest.getTrangThai())
                .build();

        // Lưu nhân viên vào cơ sở dữ liệu
        NhanVien savedNhanVien = nhanVienRepository.save(nhanVien);
        return NhanVienResponse.fromNhanVien(savedNhanVien);
    }

    @Override
    public NhanVienResponse getNhanVienById(Long id) {
        // Tìm nhân viên theo ID
        return nhanVienRepository.findById(id)
                .map(NhanVienResponse::fromNhanVien)
                .orElseThrow(() -> new AppException(ErrorCode.NHANVIEN_NOT_FOUND));
    }

    @Override
    public NhanVienResponse updateNhanVien(Long id, NhanVienRequest nhanVienRequest) {
        // Tìm nhân viên theo ID
        NhanVien nhanVien = nhanVienRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NHANVIEN_NOT_FOUND));

        // Cập nhật thông tin nhân viên
        nhanVien.setTen(nhanVienRequest.getTen());
        nhanVien.setEmail(nhanVienRequest.getEmail());
        nhanVien.setSdt(nhanVienRequest.getSdt());
        nhanVien.setAvatar(nhanVienRequest.getAvatar());
        nhanVien.setNgaySinh(nhanVienRequest.getNgaySinh());
        nhanVien.setDiaChi(nhanVienRequest.getDiaChi());
        nhanVien.setIdTaiKhoan(nhanVienRequest.getIdTaiKhoan());
        nhanVien.setGioiTinh(nhanVienRequest.getGioiTinh());
        nhanVien.setTrangThai(nhanVienRequest.getTrangThai());

        // Lưu cập nhật vào cơ sở dữ liệu
        return NhanVienResponse.fromNhanVien(nhanVienRepository.save(nhanVien));
    }

    @Override
    public String deleteNhanVien(Long id) {
        // Kiểm tra sự tồn tại của nhân viên trước khi xóa
        getNhanVienById(id);
        nhanVienRepository.deleteById(id);
        return "deleted successfully";
    }
}
