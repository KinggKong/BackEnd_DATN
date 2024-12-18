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
        return voucherRepository.findAllByFilter(tenChienDich, ngayBatDau, ngayKetThuc, trangThai, pageable)
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

        // Lưu trạng thái trước của voucher
        int oldTrangThai = voucher.getTrangThai();

        // Cập nhật thông tin voucher từ yêu cầu
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

        // Nếu trạng thái voucher thay đổi thành 0, xử lý các hóa đơn đang sử dụng voucher này
        if (voucher.getTrangThai() == 0 && oldTrangThai != 0) {
            disableVoucherInInvoices(voucher.getId());
        }

        // Nếu trạng thái voucher thay đổi từ 0 thành 1, áp dụng voucher cho các hóa đơn nếu phù hợp
        if (voucher.getTrangThai() == 1 && oldTrangThai == 0) {
            enableVoucherForInvoices(voucher.getId());
        }

        // Lưu voucher đã cập nhật vào cơ sở dữ liệu
        return VoucherResponse.fromVoucher(voucherRepository.save(voucher));
    }

    public void enableVoucherForInvoices(Long idVoucher) {
        LocalDateTime now = LocalDateTime.now();
        Voucher voucher = voucherRepository.findById(idVoucher)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        List<HoaDon> affectedInvoices = hoaDonRepository.findHoaDonAndAddVoucher(voucher.getGiaTriDonHangToiThieu());

        // Lọc các hóa đơn phù hợp và áp dụng voucher
        for (HoaDon hoaDon : affectedInvoices) {
            // Kiểm tra tính hợp lệ của voucher và áp dụng nếu hợp lệ
            if (isVoucherValid(voucher, hoaDon.getTongTien(), now)) {
                double discount = calculateDiscount(hoaDon.getTongTien(), voucher);
                hoaDon.setVoucher(voucher);  // Áp dụng voucher
                hoaDon.setSoTienGiam(discount);
                hoaDon.setTienSauGiam(hoaDon.getTongTien() - discount);
                hoaDonRepository.save(hoaDon);  // Lưu hóa đơn đã cập nhật

            } else {

            }
        }
    }

    private boolean isVoucherValid(Voucher voucher, double tongTien, LocalDateTime now) {
        return voucher.getTrangThai() == 1 &&  // Voucher đang hoạt động
                voucher.getSoLuong() > 0 &&   // Voucher còn đủ số lượng
                voucher.getNgayBatDau().isBefore(now) &&  // Voucher đã bắt đầu
                voucher.getNgayKetThuc().isAfter(now) &&  // Voucher chưa hết hạn
                tongTien >= voucher.getGiaTriDonHangToiThieu();  // Tổng tiền hóa đơn đủ điều kiện
    }


    public void disableVoucherInInvoices(Long idVoucher) {
        LocalDateTime now = LocalDateTime.now();
        Voucher voucher = voucherRepository.findById(idVoucher)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        // Lấy danh sách các hóa đơn đang sử dụng voucher này
        List<HoaDon> affectedInvoices = hoaDonRepository.findByVoucher(voucher);

        for (HoaDon hoaDon : affectedInvoices) {
            // Xóa voucher khỏi hóa đơn và đặt giá trị giảm giá về 0
            hoaDon.setVoucher(null);
            hoaDon.setSoTienGiam(0.0);
            hoaDon.setTienSauGiam(hoaDon.getTongTien());

            // Kiểm tra các voucher thay thế còn hiệu lực cho hóa đơn này
            List<Voucher> availableVouchers = voucherRepository.findAvailableVouchers(hoaDon.getTongTien());
            Voucher bestVoucher = availableVouchers.stream()
                    .filter(vo ->
                            vo.getTrangThai() == 1 && // Voucher đang hoạt động
                                    vo.getSoLuong() >= 1 && // Voucher còn đủ số lượng
                                    vo.getNgayBatDau().isBefore(now) && // Voucher đã bắt đầu
                                    vo.getNgayKetThuc().isAfter(now) && // Voucher chưa hết hạn
                                    hoaDon.getTongTien() >= vo.getGiaTriDonHangToiThieu() // Tổng tiền hóa đơn đủ điều kiện
                    )
                    .sorted(Comparator.comparingDouble((Voucher v) -> calculateDiscount(hoaDon.getTongTien(), v))
                            .reversed()) // Sắp xếp voucher theo mức giảm (tối đa)
                    .findFirst()
                    .orElse(null);

            // Nếu tìm thấy voucher thay thế, áp dụng voucher mới cho hóa đơn
            if (bestVoucher != null) {
                double discount = calculateDiscount(hoaDon.getTongTien(), bestVoucher);
                hoaDon.setVoucher(bestVoucher);
                hoaDon.setSoTienGiam(discount);
                hoaDon.setTienSauGiam(hoaDon.getTongTien() - discount);
            }

            hoaDonRepository.save(hoaDon); // Lưu hóa đơn đã cập nhật
        }
    }


    @Override
    public String deleteVoucher(Long id) {
        getVoucherById(id);
        voucherRepository.deleteById(id);
        return "deleted successfully";
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
