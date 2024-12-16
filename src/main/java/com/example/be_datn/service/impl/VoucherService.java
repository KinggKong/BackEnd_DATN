package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.VoucherRequest;
import com.example.be_datn.dto.Response.VoucherResponse;
import com.example.be_datn.entity.HoaDon;
import com.example.be_datn.entity.Voucher;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.HoaDonRepository;
import com.example.be_datn.repository.VoucherRepository;
import com.example.be_datn.service.IVoucherService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.List;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class VoucherService implements IVoucherService {
    VoucherRepository voucherRepository;
    HoaDonRepository hoaDonRepository;

    @Override
    public Page<VoucherResponse> getAllVoucherPageable(String tenChienDich, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, Integer trangThai, Pageable pageable) {
//        return voucherRepository.findVoucherByTenVoucherLike("%" + tenChienDich + "%", pageable)
//                .map(VoucherResponse::fromVoucher);
        return  voucherRepository.findAllByFilter(tenChienDich,ngayBatDau,ngayKetThuc,trangThai, pageable)
                .map(VoucherResponse::fromVoucher);
    }

    @Override
    public VoucherResponse createVoucher(VoucherRequest voucherRequest) {
        if (voucherRepository.existsVoucherByTenVoucher(voucherRequest.getMaVoucher())) {
            throw new AppException(ErrorCode.VOUCHER_ALREADY_EXISTS);
        }
        Voucher voucher = Voucher.builder()
                .tenVoucher(voucherRequest.getTenVoucher())
                .hinhThucGiam(voucherRequest.getHinhThucGiam())
                .giaTriGiam(voucherRequest.getGiaTriGiam())
                .ngayBatDau(voucherRequest.getNgayBatDau())
                .ngayKetThuc(voucherRequest.getNgayKetThuc())
                .trangThai(voucherRequest.getTrangThai())
                .maVoucher(voucherRequest.getMaVoucher())
                .giaTriDonHangToiThieu(voucherRequest.getGiaTriDonHangToiThieu())
                .giaTriGiamToiDa(voucherRequest.getGiaTriGiamToiDa())
                .soLuong(voucherRequest.getSoLuong())
                .build();
        Voucher savedVoucher = voucherRepository.save(voucher);
        return VoucherResponse.fromVoucher(savedVoucher);
    }

    @Override
    public VoucherResponse getVoucherById(Long id) {
        return voucherRepository.findById(id)
                .map(VoucherResponse::fromVoucher)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
    }

    @Override
    public VoucherResponse updateVoucher(Long idVoucher, VoucherRequest voucherRequest) {
        Voucher voucher = voucherRepository.findById(idVoucher)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        voucher.setTenVoucher(voucherRequest.getTenVoucher());
        voucher.setHinhThucGiam(voucherRequest.getHinhThucGiam());
        voucher.setGiaTriGiam(voucherRequest.getGiaTriGiam());
        voucher.setNgayBatDau(voucherRequest.getNgayBatDau());
        voucher.setNgayKetThuc(voucherRequest.getNgayKetThuc());
        voucher.setTrangThai(voucherRequest.getTrangThai());
        voucher.setMaVoucher(voucherRequest.getMaVoucher());
        voucher.setGiaTriDonHangToiThieu(voucherRequest.getGiaTriDonHangToiThieu());
        voucher.setGiaTriGiamToiDa(voucherRequest.getGiaTriGiamToiDa());
        voucher.setSoLuong(voucherRequest.getSoLuong());
        if(voucher.getTrangThai() !=1) {
            updateVoucherForInvoices(voucher.getId());
        }else {
            updateVoucherForInvoices(voucher.getId());
        }


        return VoucherResponse.fromVoucher(voucherRepository.save(voucher));
    }

    @Override
    public String deleteVoucher(Long id) {
        getVoucherById(id);
        voucherRepository.deleteById(id);
        return "deleted successfully";
    }

    public void updateVoucherForInvoices(Long idVoucher) {
        LocalDateTime now = LocalDateTime.now();
        Voucher voucher = voucherRepository.findById(idVoucher)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        if (voucher.getTrangThai() != 1) {
            List<HoaDon> affectedInvoices = hoaDonRepository.findByVoucher(voucher);

            for (HoaDon hoaDon : affectedInvoices) {
                List<Voucher> availableVouchers = voucherRepository.findAvailableVouchers(hoaDon.getTongTien());
                Voucher bestVoucher = availableVouchers.stream()
                        .filter(vo ->
                        vo.getTrangThai() == 1 &&
                                vo.getSoLuong() >= 1 &&
                                vo.getNgayBatDau().isBefore(now) &&
                                vo.getNgayKetThuc().isAfter(now) &&
                                hoaDon.getTongTien() >= vo.getGiaTriDonHangToiThieu()
                )
                        .sorted(Comparator.comparingDouble((Voucher v) -> calculateDiscount(hoaDon.getTongTien(), v))
                                .reversed())
                        .findFirst()
                        .orElse(null);
                if (bestVoucher != null) {
                    double discount = calculateDiscount(hoaDon.getTongTien(), bestVoucher);
                    hoaDon.setVoucher(bestVoucher);
                    hoaDon.setSoTienGiam(discount);
                    hoaDon.setTienSauGiam(hoaDon.getTongTien() - discount);
                } else {
                    hoaDon.setVoucher(null);
                    hoaDon.setSoTienGiam(0.0);
                    hoaDon.setTienSauGiam(hoaDon.getTongTien());
                }
                hoaDonRepository.save(hoaDon);
            }
        }
    }

    private double calculateDiscount(double totalAmount, Voucher voucher) {
        double discount;

        if (voucher.getHinhThucGiam().equalsIgnoreCase("%")) {
            discount = totalAmount * (voucher.getGiaTriGiam() / 100);
        } else {
            discount = voucher.getGiaTriGiam();
        }
        if (voucher.getGiaTriGiamToiDa() != null && discount > voucher.getGiaTriGiamToiDa()) {
            return voucher.getGiaTriGiamToiDa();
        }

        return discount;
    }

}
