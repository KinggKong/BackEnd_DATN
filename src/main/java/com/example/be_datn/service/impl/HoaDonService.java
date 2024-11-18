package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.HoaDonUpdateRequest;
import com.example.be_datn.dto.Response.HoaDonResponse;
import com.example.be_datn.entity.HoaDon;
import com.example.be_datn.entity.KhachHang;
import com.example.be_datn.entity.NhanVien;
import com.example.be_datn.entity.Voucher;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.HoaDonRepository;
import com.example.be_datn.repository.KhachHangRepository;
import com.example.be_datn.repository.VoucherRepository;
import com.example.be_datn.service.IHoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HoaDonService implements IHoaDonService {
    private final HoaDonRepository hoaDonRepository;

    private final KhachHangRepository khachHangRepository;

    private final VoucherRepository voucherRepository;


    @Override
    public Page<HoaDonResponse> getAllHoaDon(Pageable pageable) {
        return hoaDonRepository.findAllHoaDon(pageable);
    }


    @Override
    public HoaDonResponse getHoaDonById(Long id) {
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với ID: " + id));
        NhanVien nhanVien = hoaDon.getNhanVien();
        KhachHang khachHang = hoaDon.getKhachHang();
        Voucher voucher = hoaDon.getVoucher();

        return new HoaDonResponse(
                hoaDon.getId(),
                hoaDon.getMaHoaDon(),
                hoaDon.getTenNguoiNhan(),
                hoaDon.getDiaChiNhan(),
                hoaDon.getSdt(),
                hoaDon.getTongTien(),
                hoaDon.getTienSauGiam(),
                hoaDon.getTienShip(),
                hoaDon.getGhiChu(),
                hoaDon.getLoaiHoaDon(),
                hoaDon.getEmail(),
                nhanVien != null ? nhanVien.getTen() : null,
                khachHang != null ? khachHang.getTen() : null,
                hoaDon.getHinhThucThanhToan(),
                voucher != null ? voucher.getMaVoucher() : null,
                hoaDon.getSoTienGiam(),
                hoaDon.getSoLuong(),
                hoaDon.getTrangThai()
        );
    }

    @Override
    public Long createHoaDon() {
        HoaDon hoaDon = new HoaDon();
        if (hoaDon.getKhachHang() == null || hoaDon.getKhachHang().getId() == null) {
            hoaDon.setTenNguoiNhan("Khách lẻ");
        }
        hoaDon.setTrangThai(1);
        Long id = hoaDonRepository.save(hoaDon).getId();
        return id;
    }

    @Override
    public String deleteHoaDon(Long id) {
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.HOA_DON_NOT_FOUND));
        hoaDon.setTrangThai(2);
        return "Hủy đơn thành công";
    }


    @Override
    public String updateHoaDon(Long id, HoaDonUpdateRequest request) {
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.HOA_DON_NOT_FOUND));

        if (request.getIdKhachHang() == null) {
            hoaDon.setKhachHang(null);
            hoaDon.setTenNguoiNhan("Khách lẻ");
            hoaDon.setSdt("");
            hoaDon.setEmail("");
        } else {
            KhachHang khachHang = khachHangRepository.findById(request.getIdKhachHang())
                    .orElseThrow(() -> new AppException(ErrorCode.KHACH_HANG_NOT_FOUND));
            hoaDon.setKhachHang(khachHang);
            hoaDon.setTenNguoiNhan(khachHang.getTen());
            hoaDon.setEmail(khachHang.getEmail());
            hoaDon.setSdt(khachHang.getSdt());
        }

        // Kiểm tra và gán loại hóa đơn
        String loaiHoaDon = request.getLoaiHoaDon();
        if (loaiHoaDon != null) {
            hoaDon.setLoaiHoaDon(loaiHoaDon);

            if (loaiHoaDon.equals("Tại quầy")) {
                hoaDon.setTenNguoiNhan("");
                hoaDon.setDiaChiNhan("");
                hoaDon.setSdt("");
                hoaDon.setTienShip(0.0);
            }
        } else {
            throw new AppException(ErrorCode.LOAI_HOA_DON_INVALID);
        }

        // Xử lý thanh toán
//    processPayment(hoaDon);
        hoaDon.setHinhThucThanhToan(request.getHinhThucThanhToan());
        hoaDon.setGhiChu(request.getGhiChu());
        hoaDon.setTrangThai(0);

        hoaDonRepository.save(hoaDon);
        return "Update success";
    }

    @Override
    public String updateCustomer(HoaDon hoaDon, Long idKhachHang) {
        KhachHang khachHang = khachHangRepository.findById(idKhachHang)
                .orElseThrow(() -> new AppException(ErrorCode.KHACH_HANG_NOT_FOUND));
        hoaDon.setKhachHang(khachHang);
        hoaDon.setTenNguoiNhan(khachHang.getTen());
        hoaDon.setSdt(khachHang.getSdt());
        hoaDon.setEmail(khachHang.getEmail());
        hoaDonRepository.save(hoaDon);
        return "Update customer success";
    }


    public HoaDon processPayment(HoaDon hoaDon) {
        List<Voucher> eligibleVouchers = voucherRepository.findAll().stream()
                .filter(voucher -> isVoucherEligible(voucher, hoaDon))
                .collect(Collectors.toList());

        Voucher bestVoucher = findBestVoucher(eligibleVouchers, hoaDon.getTongTien());

        if (bestVoucher != null) {
            hoaDon.setVoucher(bestVoucher);
            double discount = calculateDiscount(bestVoucher, hoaDon.getTongTien());
            hoaDon.setSoTienGiam(discount);
            hoaDon.setTienSauGiam(hoaDon.getTongTien() - discount);

            bestVoucher.setSoLuong(bestVoucher.getSoLuong() - 1);
            voucherRepository.save(bestVoucher);
        } else {
            hoaDon.setSoTienGiam(0.0);
            hoaDon.setTienSauGiam(hoaDon.getTongTien());
        }

        return hoaDonRepository.save(hoaDon);
    }

    private boolean isVoucherEligible(Voucher voucher, HoaDon hoaDon) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(voucher.getNgayBatDau()) && now.isBefore(voucher.getNgayKetThuc())
                && (hoaDon.getTongTien() >= voucher.getGiaTriDonHangToiThieu())
                && (voucher.getSoLuong() > 0) && voucher.getTrangThai() == 1;
    }


    private Voucher findBestVoucher(List<Voucher> vouchers, Double orderTotal) {
        Voucher bestVoucher = null;
        double bestDiscount = 0.0;

        for (Voucher voucher : vouchers) {
            double discount = calculateDiscount(voucher, orderTotal);
            if (discount > bestDiscount) {
                bestDiscount = discount;
                bestVoucher = voucher;
            }
        }
        return bestVoucher;
    }

    private Double calculateDiscount(Voucher voucher, Double orderTotal) {
        if (voucher == null) {
            return null;
        }

        String discountType = voucher.getHinhThucGiam();
        Double discountValue = voucher.getGiaTriGiam();
        Double maxDiscount = voucher.getGiaTriGiamToiDa();

        double discount;
        switch (discountType.toLowerCase()) {
            case "%":
                discount = (orderTotal * discountValue) / 100;
                discount = Math.min(discount, maxDiscount);
                break;
            case "money":
                discount = Math.min(discountValue, orderTotal);
                break;
            default:
                throw new AppException(null);
        }

        return discount;
    }


}
