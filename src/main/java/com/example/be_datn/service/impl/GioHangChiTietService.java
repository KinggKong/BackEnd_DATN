package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.GioHangChiTietRequest;
import com.example.be_datn.dto.Response.GioHangChiTietResponse;
import com.example.be_datn.entity.GioHang;
import com.example.be_datn.entity.GioHangChiTiet;
import com.example.be_datn.entity.SanPham;
import com.example.be_datn.entity.SanPhamChiTiet;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.mapper.GioHangChiTietMapper;
import com.example.be_datn.repository.GioHangChiTietRepository;
import com.example.be_datn.repository.GioHangRepository;
import com.example.be_datn.repository.SanPhamChiTietRepository;
import com.example.be_datn.repository.SanPhamRepository;
import com.example.be_datn.service.IGioHangChiTietService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GioHangChiTietService implements IGioHangChiTietService {
    GioHangChiTietRepository gioHangChiTietRepository;
    SanPhamChiTietRepository sanPhamChiTietRepository;
    GioHangRepository gioHangRepository;
    GioHangChiTietMapper gioHangChiTietMapper;

    @Override
    public List<GioHangChiTietResponse> findByIdGioHang(Long idGioHang) {
        return gioHangChiTietMapper.toListGioHangCtResponse(gioHangChiTietRepository.findByGioHang_Id(idGioHang));
    }

    @Override
    @Transactional
    public GioHangChiTietResponse themGioHangChiTiet(GioHangChiTietRequest gioHangChiTietRequest) {
        GioHang gioHang = gioHangRepository.findById(gioHangChiTietRequest.getIdGioHang()).orElseThrow(() -> new AppException(ErrorCode.GIO_HANG_NOT_FOUND));
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(gioHangChiTietRequest.getIdSanPhamChiTiet()).orElseThrow(() -> new AppException(ErrorCode.SANPHAM_NOT_FOUND));
        GioHangChiTiet existGioHangChiTiet = gioHangChiTietRepository.findByIdGiohangAndIdSanPhamChiTiet(gioHangChiTietRequest.getIdGioHang(), gioHangChiTietRequest.getIdSanPhamChiTiet());
        if (existGioHangChiTiet != null) {
            existGioHangChiTiet.setSoLuong(existGioHangChiTiet.getSoLuong() + gioHangChiTietRequest.getSoLuong());
            existGioHangChiTiet.setGiaTien(gioHangChiTietRequest.getGiaTien());
            existGioHangChiTiet.setThoiGianGiamGia(gioHangChiTietRequest.getThoiGianGiamGia());
            return gioHangChiTietMapper.toGioHangChiTietResponse(gioHangChiTietRepository.save(existGioHangChiTiet));
        }
        GioHangChiTiet gioHangChiTiet = GioHangChiTiet.builder()
                .gioHang(gioHang)
                .soLuong(gioHangChiTietRequest.getSoLuong())
                .trangThai(1)
                .sanPhamChiTiet(sanPhamChiTiet)
                .giaTien(gioHangChiTietRequest.getGiaTien())
                .thoiGianGiamGia(gioHangChiTietRequest.getThoiGianGiamGia())
                .build();
        return gioHangChiTietMapper.toGioHangChiTietResponse(gioHangChiTietRepository.save(gioHangChiTiet));

    }



    @Override
    @Transactional
    public int xoaKhoiGioHang(Long idGioHangChiTiet) {
        gioHangChiTietRepository.findById(idGioHangChiTiet).orElseThrow(() -> new AppException(ErrorCode.GIO_HANG_CHI_TIET_NOT_FOUND));
        gioHangChiTietRepository.deleteById(idGioHangChiTiet);
        return 1;
    }

    @Override
    @Transactional
    public int xoaKhoiGioHangBySanPhamChiTiet(Long idSanPhamChiTiet, Long idGioHang) {
        return  gioHangChiTietRepository.deleteByIdSanPhamChiTiet(idSanPhamChiTiet, idGioHang);
    }

    @Override
    public void updateGioHangChiTiet(Long idSanPhamChiTiet, Long idGioHang, int soLuong) {
        // Truy vấn đối tượng GioHangChiTiet từ repository
        GioHangChiTiet gioHangChiTiet = gioHangChiTietRepository.findByIdGiohangAndIdSanPhamChiTiet(idGioHang, idSanPhamChiTiet);

        // Kiểm tra nếu gioHangChiTiet là null
        if (gioHangChiTiet == null) {
            throw new AppException(ErrorCode.GIO_HANG_CHI_TIET_NOT_FOUND); // Ném lỗi nếu không tìm thấy
        }

        // Kiểm tra số lượng hiện tại trước khi tiếp tục
        if (gioHangChiTiet.getSoLuong() < 1) {
            return; // Nếu số lượng ít hơn 1, không thực hiện cập nhật
        }

        // Cập nhật số lượng sản phẩm
        gioHangChiTiet.setSoLuong(gioHangChiTiet.getSoLuong() + soLuong);

        // Lưu lại đối tượng GioHangChiTiet đã cập nhật
        gioHangChiTietRepository.save(gioHangChiTiet);
    }

    @Override
    public void xoaHetGioHang(Long idGioHang) {
        gioHangChiTietRepository.deleteByGioHang_Id(idGioHang);
    }

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void updateSanPhamTrongGio(){
        List<GioHangChiTiet> sanPhamNgungBan = gioHangChiTietRepository.findBySanPhamChiTietTrangThai(0);
        for (GioHangChiTiet gioHangChiTiet : sanPhamNgungBan) {
            gioHangChiTiet.setTrangThai(0);
            gioHangChiTietRepository.save(gioHangChiTiet);

        }
        List<GioHangChiTiet> sanPhamDangBan = gioHangChiTietRepository.findBySanPhamChiTietTrangThai(1);
        for (GioHangChiTiet gioHangChiTiet : sanPhamDangBan) {
            gioHangChiTiet.setTrangThai(1);
            gioHangChiTietRepository.save(gioHangChiTiet);
        }
    }


}
