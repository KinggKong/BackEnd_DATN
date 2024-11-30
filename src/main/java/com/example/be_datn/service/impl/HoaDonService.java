package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.HoaDonUpdateRequest;
import com.example.be_datn.dto.Response.HoaDonResponse;
import com.example.be_datn.entity.HoaDon;
import com.example.be_datn.entity.HoaDonCT;
import com.example.be_datn.entity.KhachHang;
import com.example.be_datn.entity.LichSuHoaDon;
import com.example.be_datn.entity.NhanVien;
import com.example.be_datn.entity.SanPhamChiTiet;
import com.example.be_datn.entity.StatusPayment;
import com.example.be_datn.entity.TypeBill;
import com.example.be_datn.entity.Voucher;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.HoaDonChiTietRepository;
import com.example.be_datn.repository.HoaDonRepository;
import com.example.be_datn.repository.KhachHangRepository;
import com.example.be_datn.repository.LichSuHoaDonRepository;
import com.example.be_datn.repository.SanPhamChiTietRepository;
import com.example.be_datn.repository.VoucherRepository;
import com.example.be_datn.service.IHoaDonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HoaDonService implements IHoaDonService {
    private final HoaDonRepository hoaDonRepository;

    private final KhachHangRepository khachHangRepository;

    private final LichSuHoaDonRepository lichSuHoaDonRepository;

    private final HoaDonChiTietRepository hoaDonChiTietRepository;

    private final VoucherRepository voucherRepository;
    private final SanPhamChiTietService sanPhamChiTietService;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;

    public String generateInvoiceCode() {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        Random random = new Random();
        int randomNumber = 10000 + random.nextInt(90000);
        return "INV-" + date + "-" + randomNumber;
    }


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
                hoaDon.getTrangThai(),
                voucher != null ? voucher.getMaVoucher() : null,
                hoaDon.getSoTienGiam(),
                hoaDon.getCreated_at(),
                hoaDon.getUpdated_at()
        );
    }

    @Override
    public Long createHoaDon() {
        HoaDon hoaDon = new HoaDon();
        KhachHang khachHangLe = khachHangRepository.findByTen("Khách lẻ");
        hoaDon.setKhachHang(khachHangLe);
        hoaDon.setTongTien(0.0);
        hoaDon.setHinhThucThanhToan(null);
        hoaDon.setLoaiHoaDon(String.valueOf(TypeBill.OFFLINE));
        hoaDon.setTrangThai(String.valueOf(StatusPayment.PENDING));
        hoaDon.setMaHoaDon(generateInvoiceCode());
        // try
        hoaDon.setTenNguoiNhan(khachHangLe.getTen());
        hoaDonRepository.save(hoaDon);
        Long id = hoaDonRepository.save(hoaDon).getId();
        LichSuHoaDon lichSuHoaDon = LichSuHoaDon.builder()
                .nhanVien(null)
                .hoaDon(hoaDon)
                .ghiChu("PENDING")
                .createdBy(null)
                .trangThai(StatusPayment.PENDING.toString())
                .build();
        lichSuHoaDonRepository.save(lichSuHoaDon);
        return 1L;
    }

    @Override
    public String deleteHoaDon(Long id) {
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.HOA_DON_NOT_FOUND));
        hoaDon.setTrangThai(String.valueOf(StatusPayment.CANCELLED));
        LichSuHoaDon lichSuHoaDon = lichSuHoaDonRepository.findByHoaDon_Id(hoaDon.getId()).get(0).builder()
                .nhanVien(null)
                .hoaDon(hoaDon)
                .ghiChu("CANCELLED")
                .createdBy(null)
                .trangThai(StatusPayment.CANCELLED.toString())
                .build();
        lichSuHoaDonRepository.save(lichSuHoaDon);
        hoaDonRepository.save(hoaDon);
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

        String loaiHoaDon = request.getLoaiHoaDon();
        if (loaiHoaDon != null) {
            hoaDon.setLoaiHoaDon(loaiHoaDon);
            if (loaiHoaDon.equals(TypeBill.OFFLINE)) {
                hoaDon.setDiaChiNhan("");
                hoaDon.setSdt("");
                hoaDon.setTienShip(0.0);
            }
        } else {
            throw new AppException(ErrorCode.LOAI_HOA_DON_INVALID);
        }

        hoaDon.setHinhThucThanhToan(request.getHinhThucThanhToan());
        hoaDon.setGhiChu(request.getGhiChu());
        hoaDon.setTrangThai(String.valueOf(StatusPayment.DONE));

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

    @Override
    @Transactional
    public HoaDonResponse completeHoaDon(Long id) {
        // Retrieve the HoaDon entity or throw an exception if not found
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.HOA_DON_NOT_FOUND));

        // Set the HoaDon status to DONE
        hoaDon.setTrangThai(StatusPayment.DONE.toString());

        // Save the updated HoaDon entity
        HoaDon updatedHoaDon = hoaDonRepository.save(hoaDon);

        // Retrieve the corresponding LichSuHoaDon, ensure it's not null
        LichSuHoaDon lichSuHoaDon = lichSuHoaDonRepository.findLichSuHoaDonByHoaDonId(updatedHoaDon.getId());

        // Check if LichSuHoaDon is null, and handle accordingly
        if (lichSuHoaDon == null) {
            // Optionally, throw an exception if you expect a LichSuHoaDon record to exist
            throw new AppException(ErrorCode.LICH_SU_HOA_DON_NOT_FOUND);

            // Or create a new LichSuHoaDon if this is an expected case (e.g., new record creation)
            // lichSuHoaDon = new LichSuHoaDon();
            // lichSuHoaDon.setHoaDon(updatedHoaDon);
            // lichSuHoaDon.setGhiChu("DONE");
            // lichSuHoaDon.setTrangThai(StatusPayment.DONE.toString());
            // lichSuHoaDonRepository.save(lichSuHoaDon);
        } else {
            // Update the existing LichSuHoaDon
            lichSuHoaDon.setGhiChu("DONE");
            lichSuHoaDon.setTrangThai(StatusPayment.DONE.toString());
            lichSuHoaDonRepository.save(lichSuHoaDon);
        }

        // Return a response object with the updated HoaDon data
        return new HoaDonResponse(
                updatedHoaDon.getId(),
                updatedHoaDon.getMaHoaDon(),
                updatedHoaDon.getTenNguoiNhan(),
                updatedHoaDon.getDiaChiNhan(),
                updatedHoaDon.getSdt(),
                updatedHoaDon.getTongTien(),
                updatedHoaDon.getTienSauGiam(),
                updatedHoaDon.getTienShip(),
                updatedHoaDon.getGhiChu(),
                updatedHoaDon.getLoaiHoaDon(),
                updatedHoaDon.getEmail(),
                updatedHoaDon.getNhanVien() != null ? updatedHoaDon.getNhanVien().getTen() : null,
                updatedHoaDon.getKhachHang() != null ? updatedHoaDon.getKhachHang().getTen() : null,
                updatedHoaDon.getHinhThucThanhToan(),
                updatedHoaDon.getTrangThai(),
                updatedHoaDon.getVoucher() != null ? updatedHoaDon.getVoucher().getMaVoucher() : null,
                updatedHoaDon.getSoTienGiam(),
                updatedHoaDon.getCreated_at(),
                updatedHoaDon.getUpdated_at()
        );
    }


    public Voucher selectVoucher(Long idVoucher) {
        LocalDateTime time = LocalDateTime.now();
        Voucher voucher = voucherRepository.findById(idVoucher)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        if (voucher.getNgayBatDau().isAfter(time)) {
            throw new AppException(ErrorCode.VOUCHER_NOT_YET_STARTED);
        }
        if (voucher.getNgayKetThuc().isBefore(time)) {
            throw new AppException(ErrorCode.VOUCHER_EXPIRED);
        }
        return voucher;
    }

    public Voucher selectTheBestVoucher(Long idHoaDon, List<Voucher> voucherList) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new AppException(ErrorCode.HOA_DON_NOT_FOUND));
//        Voucher theBestVoucher = null;
//        double maxDiscount = 0.0;
//
//        for(Voucher voucher : voucherList) {
//            if(hoaDon.getTongTien() >= voucher.getGiaTriDonHangToiThieu()) {
//                double discount = calculateDiscount(hoaDon.getTongTien(), voucher);
//                if (discount > maxDiscount) {
//                    theBestVoucher = voucher;
//                    maxDiscount = discount;
//                }
//            }
//        }
//        return theBestVoucher;
        List<Voucher> validVouchers = voucherList.stream()
                .filter(voucher -> voucher.getTrangThai() ==1  && hoaDon.getTongTien() >= voucher.getGiaTriDonHangToiThieu())
                .sorted(Comparator.comparingDouble((Voucher voucher) -> calculateDiscount(hoaDon.getId(), voucher))
                        .reversed())
                .collect(Collectors.toList());
        return validVouchers.stream()
                .findFirst()
                .orElse(null);
     }

    private double calculateDiscount(double totalAmount, Voucher voucher) {
        if (voucher.getHinhThucGiam().equalsIgnoreCase("%")) {
            return totalAmount * (voucher.getGiaTriGiam() / 100);
        } else {
            return voucher.getGiaTriGiam();
        }
    }




    @Transactional
    @Scheduled(fixedRate = 60000)
    public void cancelExpiredOrders() {
        LocalDateTime twoHoursAgo = LocalDateTime.now().minusHours(2);

        List<HoaDon> ordersToCancel = hoaDonRepository.selectOrderWhereStatusIsPending();
        for (HoaDon order : ordersToCancel) {
            if (order.getCreated_at().isBefore(twoHoursAgo)) {
                for (HoaDonCT detail : order.getChiTietList()) {
                    SanPhamChiTiet spct = detail.getSanPhamChiTiet();
                    spct.setSoLuong(spct.getSoLuong() + detail.getSoLuong());
                    sanPhamChiTietRepository.save(spct);
                }
                hoaDonChiTietRepository.deleteAll(order.getChiTietList());
                lichSuHoaDonRepository.deleteById(order.getId());
                hoaDonRepository.delete(order);
            }
        }
    }
}
