package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.HoaDonUpdateRequest;
import com.example.be_datn.dto.Response.HoaDonResponse;
import com.example.be_datn.entity.HoaDon;
import com.example.be_datn.entity.HoaDonCT;
import com.example.be_datn.entity.KhachHang;
import com.example.be_datn.entity.LichSuHoaDon;
import com.example.be_datn.entity.LichSuThanhToan;
import com.example.be_datn.entity.NhanVien;
import com.example.be_datn.entity.SanPhamChiTiet;
import com.example.be_datn.entity.StatusPayment;
import com.example.be_datn.entity.TypeBill;
import com.example.be_datn.entity.Voucher;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.mapper.HoaDonMapper;
import com.example.be_datn.repository.HoaDonChiTietRepository;
import com.example.be_datn.repository.HoaDonRepository;
import com.example.be_datn.repository.KhachHangRepository;
import com.example.be_datn.repository.LichSuHoaDonRepository;
import com.example.be_datn.repository.LichSuThanhToanRepository;
import com.example.be_datn.repository.SanPhamChiTietRepository;
import com.example.be_datn.repository.VoucherRepository;
import com.example.be_datn.service.IHoaDonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HoaDonService implements IHoaDonService {
    private final HoaDonRepository hoaDonRepository;

    private final KhachHangRepository khachHangRepository;

    private final LichSuHoaDonRepository lichSuHoaDonRepository;

    private final HoaDonChiTietRepository hoaDonChiTietRepository;

    private final LichSuThanhToanRepository lichSuThanhToanRepository;

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
        hoaDon.setTenNguoiNhan(khachHangLe.getTen());
        hoaDon.setDiaChiNhan("");
        hoaDon.setHinhThucThanhToan("");
        hoaDon.setSdt("");
        hoaDon.setTienSauGiam(0.0);
        hoaDon.setTienShip(0.0);
        hoaDon.setTongTien(0.0);

        HoaDon hd = hoaDonRepository.save(hoaDon);
        LichSuHoaDon lichSuHoaDon = LichSuHoaDon.builder()
                .nhanVien(null)
                .hoaDon(hd)
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


    HoaDonMapper hoaDonMapper;


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
            }else {
                hoaDon.setTienShip(request.getTienShip());
                hoaDon.setDiaChiNhan(request.getDiaChiNhan());
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
    public String completeHoaDon(Long id, String method) {
        // Retrieve the HoaDon object
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.HOA_DON_NOT_FOUND));

        // Check if the HoaDon has a voucher before attempting any voucher-related logic
        if (hoaDon.getVoucher() != null) {
            // Process the voucher if it exists
            Voucher voucher = voucherRepository.findById(hoaDon.getVoucher().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

            // Check if the voucher quantity is available
            if (voucher.getSoLuong() <= 0) {
                throw new AppException(ErrorCode.VOUCHER_EXPIRED);
            }

            // Update the voucher quantity and save
            voucher.setSoLuong(voucher.getSoLuong() - 1);
            voucherRepository.save(voucher);
            double discount = hoaDon.getSoTienGiam();
            hoaDon.setTienSauGiam(hoaDon.getTongTien() - discount);
        } else {
            hoaDon.setSoTienGiam(0.0);
            hoaDon.setTienSauGiam(hoaDon.getTongTien());
        }
        hoaDon.setHinhThucThanhToan(method);
        hoaDon.setTrangThai(StatusPayment.DONE.toString());

        HoaDon updatedHoaDon = hoaDonRepository.save(hoaDon);

        LichSuHoaDon lichSuHoaDon = lichSuHoaDonRepository.findLichSuHoaDonByHoaDonId(updatedHoaDon.getId());

        if (lichSuHoaDon == null) {
            throw new AppException(ErrorCode.LICH_SU_HOA_DON_NOT_FOUND);
        } else {
            // Update the existing LichSuHoaDon
            lichSuHoaDon.setGhiChu("DONE");
            lichSuHoaDon.setTrangThai(StatusPayment.DONE.toString());
            lichSuHoaDonRepository.save(lichSuHoaDon);
        }

        // Log payment transaction (LichSuThanhToan)
        LichSuThanhToan lichSuThanhToan = LichSuThanhToan.builder()
                .soTien(updatedHoaDon.getTongTien())
                .paymentMethod(updatedHoaDon.getHinhThucThanhToan())
                .type(hoaDon.getLoaiHoaDon())
                .hoaDon(updatedHoaDon)
                .status(StatusPayment.DONE.toString())
                .build();
        lichSuThanhToanRepository.save(lichSuThanhToan);

        return "ok";
    }



    @Transactional
    @Scheduled(fixedRate = 600000)
    public void cancelExpiredOrders() {
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusHours(2); // Sử dụng 1 phút thay vì 2 giờ

        List<HoaDon> ordersToCancel = hoaDonRepository.selectOrderWhereStatusIsPending();
        for (HoaDon order : ordersToCancel) {
            if (order.getCreated_at().isBefore(oneMinuteAgo)) {  // Kiểm tra xem hóa đơn có quá 1 phút hay không
                // Xóa lịch sử hóa đơn trước khi xóa hóa đơn
                lichSuHoaDonRepository.deleteByHoaDon_Id(order.getId()); // Xóa tất cả bản ghi trong lịch sử hóa đơn
                // Lặp qua tất cả chi tiết hóa đơn và cập nhật lại số lượng sản phẩm
                for (HoaDonCT detail : order.getChiTietList()) {
                    SanPhamChiTiet spct = detail.getSanPhamChiTiet();
                    spct.setSoLuong(spct.getSoLuong() + detail.getSoLuong());  // Cập nhật số lượng sản phẩm
                    sanPhamChiTietRepository.save(spct);
                }

                hoaDonChiTietRepository.deleteByHoaDon_Id(order.getId());
                hoaDonRepository.delete(order);
            }
        }
    }



    @Override
    public HoaDonResponse findByMaHoaDon(String maHoaDon) {
        return hoaDonMapper.toHoaDonResponse(hoaDonRepository.findByMaHoaDon(maHoaDon));
    }

    @Override
    public void changeTypeBill(Long idHoaDon) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new AppException(ErrorCode.HOA_DON_NOT_FOUND));
        if(hoaDon.getLoaiHoaDon().equalsIgnoreCase(TypeBill.OFFLINE.toString())) {
            hoaDon.setLoaiHoaDon(TypeBill.ONLINE.toString());
        }else {
            hoaDon.setLoaiHoaDon(TypeBill.OFFLINE.toString());
        }
        hoaDonRepository.save(hoaDon);
    }
}