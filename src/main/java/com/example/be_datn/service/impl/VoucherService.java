package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.VoucherRequest;
import com.example.be_datn.dto.Response.VoucherResponse;
import com.example.be_datn.entity.Voucher;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.VoucherRepository;
import com.example.be_datn.service.IVoucherService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class VoucherService implements IVoucherService {
    VoucherRepository voucherRepository;

    @Override
    public Page<VoucherResponse> getAllVoucherPageable(String tenChienDich, Pageable pageable) {
        return voucherRepository.findVoucherByTenVoucherLike("%" + tenChienDich + "%", pageable)
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

        return VoucherResponse.fromVoucher(voucherRepository.save(voucher));
    }

    @Override
    public String deleteVoucher(Long id) {
        getVoucherById(id);
        voucherRepository.deleteById(id);
        return "deleted successfully";
    }
}
