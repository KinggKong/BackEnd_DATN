package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.StatusBillRequest;
import com.example.be_datn.dto.Response.LichSuHoaDonResponse;
import com.example.be_datn.entity.*;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.mapper.LichSuHoaDonMapper;
import com.example.be_datn.repository.*;
import com.example.be_datn.service.ILichSuHoaDonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LichSuHoaDonService implements ILichSuHoaDonService {
    LichSuHoaDonRepository lichSuHoaDonRepository;
    LichSuHoaDonMapper lichSuHoaDonMapper;
    NhanVienRepository nhanVienRepository;
    HoaDonRepository hoaDonRepository;
    private final LichSuThanhToanRepository lichSuThanhToanRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final VoucherRepository voucherRepository;
    private final GioHangChiTietRepository gioHangChiTietRepository;

    @Override
    public LichSuHoaDonResponse insertLichSuHoaDon(StatusBillRequest statusBillRequest) {
        NhanVien nhanVien = new NhanVien();
        if (statusBillRequest.getStatus().equals(StatusPayment.CANCELLED.toString())) {
            HoaDon hoaDon = hoaDonRepository.findById(statusBillRequest.getIdHoaDon()).orElseThrow(() -> new AppException(ErrorCode.HOA_DON_NOT_FOUND));

            if (hoaDon.getTrangThai().equalsIgnoreCase(StatusPayment.SHIPPING.toString()) || hoaDon.getTrangThai().equalsIgnoreCase(StatusPayment.DONE.toString())) {
                throw new AppException(ErrorCode.CANT_BE_CANCEL_WHEN_SHIPPING);
            }

            if (hoaDon.getTrangThai().equals(StatusPayment.ACCEPTED.toString())) {
                updateAfterCancel(statusBillRequest.getIdHoaDon());
            }
            if (hoaDon.getVoucher() != null) {
                Voucher voucher = voucherRepository.findById(hoaDon.getVoucher().getId()).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
                if (voucher != null) {
                    voucher.setSoLuong(voucher.getSoLuong() + 1);
                    voucherRepository.saveAndFlush(voucher);
                }
            }
        }
        if (statusBillRequest.getStatus().equals(StatusPayment.ACCEPTED.toString())) {
            handleUpdateAfterBuy(statusBillRequest.getIdHoaDon());
        }

        if (statusBillRequest.getIdNhanvien() == null) {
            NhanVien existedNhanVien = nhanVienRepository.findByTen("BOT");
            if (existedNhanVien == null) {
                NhanVien newNhanVien = NhanVien.builder()
                        .ten("BOT")
                        .build();
                nhanVien = nhanVienRepository.saveAndFlush(newNhanVien);
            } else {
                nhanVien = existedNhanVien;
            }
        } else {
            nhanVien = nhanVienRepository.findById(statusBillRequest.getIdNhanvien()).orElseThrow(() -> new AppException(ErrorCode.NHANVIEN_NOT_FOUND));
        }


        HoaDon hoaDon = hoaDonRepository.findById(statusBillRequest.getIdHoaDon()).orElseThrow(() -> new AppException(ErrorCode.HOA_DON_NOT_FOUND));

        LichSuHoaDon lichSuHoaDon = LichSuHoaDon.builder()
                .nhanVien(nhanVien)
                .hoaDon(hoaDon)
                .createdBy(nhanVien.getTen())
                .trangThai(statusBillRequest.getStatus())
                .ghiChu(statusBillRequest.getGhiChu())
                .createdBy(nhanVien.getTen())
                .build();
        LichSuHoaDon lichSuHD = lichSuHoaDonRepository.save(lichSuHoaDon);
        hoaDon.setTrangThai(lichSuHD.getTrangThai());
        hoaDonRepository.save(hoaDon);

        if (statusBillRequest.getStatus().equals(StatusPayment.DONE.toString())) {
            LichSuThanhToan lichSuThanhToan = LichSuThanhToan.builder()
                    .hoaDon(hoaDon)
                    .type(hoaDon.getLoaiHoaDon())
                    .soTien(hoaDon.getTongTien() - hoaDon.getSoTienGiam())
                    .paymentMethod(hoaDon.getHinhThucThanhToan())
                    .status("DONE")
                    .build();
            lichSuThanhToanRepository.save(lichSuThanhToan);
        }
        return lichSuHoaDonMapper.toResponse(lichSuHD);
    }

    @Override
    public List<LichSuHoaDonResponse> findLichSuHoaDonByIdHoaDon(Long idHoaDon) {
        List<LichSuHoaDon> lichSuHoaDons = lichSuHoaDonRepository.findByHoaDon_Id(idHoaDon);
        return lichSuHoaDonMapper.toListResponse(lichSuHoaDons);
    }

    public void updateAfterCancel(Long idHoaDon) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon).orElseThrow(() -> new AppException(ErrorCode.HOA_DON_NOT_FOUND));

//        if (hoaDon.getVoucher() != null) {
//            Voucher voucher = voucherRepository.findById(hoaDon.getVoucher().getId()).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
//            if (voucher != null) {
//                voucher.setSoLuong(voucher.getSoLuong() + 1);
//                voucherRepository.saveAndFlush(voucher);
//            }
//        }

        List<HoaDonCT> hoaDonCTList = hoaDonChiTietRepository.findByHoaDon_Id(idHoaDon);
        List<SanPhamChiTiet> newSanPhamChiTiets = new ArrayList<>();

        hoaDonCTList.stream().forEach(hdct -> {
            int soLuongGioHang = hdct.getSoLuong();
            int soLuongSanPhamChiTiet = hdct.getSanPhamChiTiet().getSoLuong();

            hdct.getSanPhamChiTiet().setSoLuong(soLuongSanPhamChiTiet + soLuongGioHang);
            newSanPhamChiTiets.add(hdct.getSanPhamChiTiet());
        });
        sanPhamChiTietRepository.saveAll(newSanPhamChiTiets);
    }

    public void handleUpdateAfterBuy(Long idHoaDon) {
        if (idHoaDon == null) {
            throw new AppException(ErrorCode.HOA_DON_NOT_FOUND);
        }

        List<HoaDonCT> hoaDonCTs = hoaDonChiTietRepository.findByHoaDon_Id(idHoaDon);
        if (hoaDonCTs.isEmpty()) {
            throw new AppException(ErrorCode.HOA_DON_CHI_TIET_NOT_FOUND_LIST);
        }

        List<SanPhamChiTiet> updatedProducts = new ArrayList<>();
        for (HoaDonCT hoaDonCT : hoaDonCTs) {
            SanPhamChiTiet sanPhamChiTiet = hoaDonCT.getSanPhamChiTiet();
            int soLuongTonKho = sanPhamChiTiet.getSoLuong();
            int soLuongTrongGio = hoaDonCT.getSoLuong();

            if (soLuongTonKho < soLuongTrongGio) {
                throw new AppException(ErrorCode.SOLUONG_SANPHAM_KHONG_DU);
            }

            sanPhamChiTiet.setSoLuong(soLuongTonKho - soLuongTrongGio);
            updatedProducts.add(sanPhamChiTiet);
        }

        sanPhamChiTietRepository.saveAll(updatedProducts);
    }
}
