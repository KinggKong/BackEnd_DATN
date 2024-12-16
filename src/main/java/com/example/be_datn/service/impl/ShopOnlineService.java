package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.HoaDonRequest;
import com.example.be_datn.dto.Response.*;
import com.example.be_datn.entity.*;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.mapper.HoaDonChiTietMapper;
import com.example.be_datn.mapper.HoaDonMapper;
import com.example.be_datn.mapper.LichSuHoaDonMapper;
import com.example.be_datn.mapper.LichSuThanhToanMapper;
import com.example.be_datn.repository.*;
import com.example.be_datn.service.IShopOnlineService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ShopOnlineService implements IShopOnlineService {
    GioHangChiTietService gioHangChiTietService;
    GioHangRepository gioHangRepository;
    HoaDonRepository hoaDonRepository;
    HoaDonChiTietRepository hoaDonChiTietRepository;
    VoucherRepository voucherRepository;
    NhanVienRepository nhanVienRepository;
    LichSuHoaDonRepository lichSuHoaDonRepository;
    HoaDonChiTietMapper hoaDonChiTietMapper;
    HoaDonMapper hoaDonMapper;
    LichSuHoaDonMapper lichSuHoaDonMapper;
    EmailService emailService;
    LichSuThanhToanMapper lichSuThanhToanMapper;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 10;
    private final GioHangChiTietRepository gioHangChiTietRepository;
    private final LichSuThanhToanRepository lichSuThanhToanRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;

    public String generateBillCode() {
        StringBuilder billCode = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            billCode.append(CHARACTERS.charAt(index));
        }
        return billCode.toString();
    }

    @Override
    public AboutProductShopOn preCheckout(Long idKhachHang) {
        GioHang gioHang = gioHangRepository.findByKhachHang_Id(idKhachHang);
        if (gioHang == null) {
            throw new AppException(ErrorCode.GIO_HANG_NOT_FOUND);
        }
        List<GioHangChiTietResponse> gioHangChiTietResponses = gioHangChiTietService.findByIdGioHang(gioHang.getId());
        if (gioHangChiTietResponses.isEmpty()) {
            return new AboutProductShopOn();
        }
        double totalPrice = 0;
        for (GioHangChiTietResponse gioHangChiTietResponse : gioHangChiTietResponses) {
            totalPrice += gioHangChiTietResponse.getGiaTien() * gioHangChiTietResponse.getSoLuong();
        }
        return AboutProductShopOn.builder()
                .idGioHang(gioHang.getId())
                .gioHangChiTietList(gioHangChiTietResponses)
                .totalPrice(totalPrice)
                .build();
    }

    @Override
    public ApiResponse<?> checkout(HoaDonRequest hoaDonRequest) {
        if (hoaDonRequest.getIdVoucher() != null) {
            if (checkVourcherCanUse(hoaDonRequest.getIdVoucher()) == null) {
                return ApiResponse.builder()
                        .code(2000)
                        .data("Voucher đã không còn hoạt động")
                        .message("Voucher đã không còn hoạt động")
                        .build();
            }
        }

        checkSoLuongHopLe(hoaDonRequest.getIdGioHang());
        handleUpdateAfterBuy(hoaDonRequest.getIdGioHang());
        HoaDon hd = createHoaDon(hoaDonRequest);
        HoaDon hoaDon = hoaDonRepository.saveAndFlush(hd);

        LichSuHoaDon lichSuHoaDon = LichSuHoaDon.builder()
                .nhanVien(null)
                .hoaDon(hoaDon)
                .createdBy(null)
                .ghiChu("Chờ xác nhận")
                .trangThai(StatusPayment.WAITING.toString())
                .build();

        lichSuHoaDonRepository.saveAndFlush(lichSuHoaDon);

        if (hoaDon == null) {
            throw new AppException(ErrorCode.HOA_DON_INVALID);
        }

        List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietRepository.findByGioHang_IdAndTrangThai(hoaDonRequest.getIdGioHang(), 1);
        gioHangChiTietList.stream()
                .map(gioHangChiTiet -> hoaDonChiTietMapper.toHoaDonCT(gioHangChiTiet, hoaDon.getId()))
                .forEach(hoaDonChiTietRepository::saveAndFlush);

        gioHangChiTietRepository.deleteByGioHang_Id(hoaDonRequest.getIdGioHang());


        emailService.sendMailToUser(hoaDonRequest.getEmail(), "3HST Shoes - Cảm ơn bạn đã đặt hàng tại 3HST Shoes", hoaDon.getMaHoaDon(), this.getInfoOrder(hoaDon.getMaHoaDon()));
        return ApiResponse.builder()
                .data(hoaDonMapper.toHoaDonResponse(hoaDon))
                .message("checkout successfully")
                .build();
    }

    public HoaDonResponse checkoutOnline(HoaDonRequest hoaDonRequest) {
        HoaDon hd = createHoaDon(hoaDonRequest);
        HoaDon hoaDon = hoaDonRepository.saveAndFlush(hd);

        LichSuHoaDon lichSuHoaDon = LichSuHoaDon.builder()
                .nhanVien(null)
                .hoaDon(hoaDon)
                .createdBy(null)
                .ghiChu("Chờ xác nhận")
                .trangThai(StatusPayment.WAITING.toString())
                .build();

        lichSuHoaDonRepository.saveAndFlush(lichSuHoaDon);

        if (hoaDon == null) {
            throw new AppException(ErrorCode.HOA_DON_INVALID);
        }

        List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietRepository.findByGioHang_IdAndTrangThai(hoaDonRequest.getIdGioHang(), 1);
        gioHangChiTietList.stream()
                .map(gioHangChiTiet -> hoaDonChiTietMapper.toHoaDonCT(gioHangChiTiet, hoaDon.getId()))
                .forEach(hoaDonChiTietRepository::saveAndFlush);
        emailService.sendMailToUser(hoaDonRequest.getEmail(), "3HST Shoes - Cảm ơn bạn đã đặt hàng tại 3HST Shoes", hoaDon.getMaHoaDon(), this.getInfoOrder(hoaDon.getMaHoaDon()));
        return hoaDonMapper.toHoaDonResponse(hoaDon);
    }


    @Override
    public InfoOrder getInfoOrder(String maHoaDon) {
        if (maHoaDon.isBlank()) {
            throw new AppException(ErrorCode.HOA_DON_NOT_FOUND);
        }
        HoaDon hoaDon = hoaDonRepository.findByMaHoaDon(maHoaDon);
        if (hoaDon == null) {
            throw new AppException(ErrorCode.HOA_DON_NOT_FOUND);
        }
        List<HoaDonCT> hoaDonCTList = hoaDonChiTietRepository.findByHoaDon_Id(hoaDon.getId());
        double tongTienHang = hoaDonCTList.stream().mapToDouble(item -> item.getGiaTien() * item.getSoLuong()).sum();
        if (hoaDonCTList.isEmpty()) {
            throw new AppException(ErrorCode.HOA_DON_CHI_TIET_NOT_FOUND_LIST);
        }
        return InfoOrder.builder()
                .hoaDonResponse(hoaDonMapper.toHoaDonResponse(hoaDon))
                .hoaDonChiTietResponse(hoaDonChiTietMapper.toListResponse(hoaDonCTList))
                .tongTienHang(tongTienHang)
                .build();
    }

    @Override
    public List<HoaDonResponse> getAllOrderByStatus(String trangThai, String keySearch) {
        List<HoaDon> hoaDons = hoaDonRepository.findByTrangThai(trangThai, keySearch);
        if (trangThai.contains("WAITING")) {
//            hoaDons = hoaDons.stream()
//                    .sorted(Comparator.comparing(HoaDon::getCreated_at))
//                    .collect(Collectors.toList());
            Collections.reverse(hoaDons);
        }
        if (hoaDons.isEmpty()) {
            return new ArrayList<>();
        }
        return hoaDonMapper.toListResponse(hoaDons);
    }

    @Override
    public DetailHistoryBillResponse getDetailHistoryBill(Long idHoaDon) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon).orElseThrow(() -> new AppException(ErrorCode.HOA_DON_NOT_FOUND));
        HoaDonResponse hoaDonResponse = hoaDonMapper.toHoaDonResponse(hoaDon);
        List<LichSuHoaDonResponse> lichSuHoaDonResponses = lichSuHoaDonMapper.toListResponse(lichSuHoaDonRepository.findByHoaDon_Id(idHoaDon));
        List<HoaDonChiTietResponse> hoaDonChiTietResponses = hoaDonChiTietMapper.toListResponse(hoaDonChiTietRepository.findByHoaDon_Id(hoaDon.getId()));
        LichSuThanhToan lichSuThanhToan = lichSuThanhToanRepository.findByHoaDon_Id(hoaDon.getId());
        LichSuThanhToanResponse lichSuThanhToanResponse = lichSuThanhToan != null ? lichSuThanhToanMapper.toResponse(lichSuThanhToan) : null;
        return DetailHistoryBillResponse.builder()
                .hoaDonResponse(hoaDonResponse)
                .lichSuHoaDonResponses(lichSuHoaDonResponses)
                .hoaDonChiTietResponses(hoaDonChiTietResponses)
                .lichSuThanhToanResponse(lichSuThanhToanResponse)
                .build();
    }

    @Override
    public Page<HistoryBillResponse> getAllHistoryBill(Long idKhachHang, int pageNumber, int pageSize) {
        pageNumber -= 1;
        pageNumber = Math.max(0, pageNumber);
        pageSize = Math.max(10, pageSize);

        List<HistoryBillResponse> historyBillResponses = hoaDonRepository.getAllHistoryBillByIdKhachHang(idKhachHang);
        List<HistoryBillResponse> newList = historyBillResponses.stream().peek(his -> {
            List<HoaDonCT> hoaDonCTList = hoaDonChiTietRepository.findByHoaDon_Id(his.getId());
            his.setHoaDonChiTietResponse(hoaDonChiTietMapper.toListResponse(hoaDonCTList));
        }).toList();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<HistoryBillResponse> pages = new PageImpl<HistoryBillResponse>(newList, pageable, newList.size());
        return pages;
    }


    public HoaDon createHoaDon(HoaDonRequest hoaDonRequest) {

        KhachHang khachHang = KhachHang.builder()
                .id(1L)
                .build();
        Voucher voucher = null;
        if (hoaDonRequest.getIdVoucher() != null) {
            voucher = voucherRepository.findById(hoaDonRequest.getIdVoucher()).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        }

        return HoaDon.builder()
                .maHoaDon(generateBillCode())
                .tenNguoiNhan(hoaDonRequest.getTenNguoiNhan())
                .sdt(hoaDonRequest.getSdt())
                .tongTien(hoaDonRequest.getTongTien())
                .diaChiNhan(hoaDonRequest.getDiaChiNhan())
                .tienSauGiam(hoaDonRequest.getTienSauGiam())
                .tienShip(hoaDonRequest.getTienShip())
                .ghiChu(hoaDonRequest.getGhiChu())
                .loaiHoaDon(TypeBill.ONLINE.toString())
                .email(hoaDonRequest.getEmail())
                .nhanVien(null)
                .khachHang(khachHang)
                .hinhThucThanhToan(hoaDonRequest.getHinhThucThanhToan())
                .trangThai(StatusPayment.WAITING.toString())
                .voucher(voucher)
                .soTienGiam(hoaDonRequest.getSoTienGiam())
                .build();
    }

    public void clearGioHangChiTiet(Long idGioHang) {
        gioHangChiTietRepository.deleteByGioHang_Id(idGioHang);
    }

    void checkSoLuongHopLe(Long idGioHang) {
        List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietRepository.findByGioHang_IdAndTrangThai(idGioHang, 1);
        gioHangChiTietList.forEach(gioHangChiTiet -> {
            if (gioHangChiTiet.getSoLuong() > gioHangChiTiet.getSanPhamChiTiet().getSoLuong()) {
                throw new AppException(ErrorCode.SOLUONG_SANPHAM_KHONG_DU);
            }
        });
    }

    public void handleUpdateAfterBuy(Long idGioHang) {
        if (idGioHang == null) {
            throw new AppException(ErrorCode.ID_GIO_HANG_CANT_BE_NULL);
        }

        List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietRepository.findByGioHang_IdAndTrangThai(idGioHang, 1);
        if (gioHangChiTietList.isEmpty()) {
            throw new AppException(ErrorCode.CART_DONT_HAVE_PRODUCT);
        }

        List<SanPhamChiTiet> updatedProducts = new ArrayList<>();
        for (GioHangChiTiet gioHangChiTiet : gioHangChiTietList) {
            SanPhamChiTiet sanPhamChiTiet = gioHangChiTiet.getSanPhamChiTiet();
            int soLuongTonKho = sanPhamChiTiet.getSoLuong();
            int soLuongTrongGio = gioHangChiTiet.getSoLuong();

            if (soLuongTonKho < soLuongTrongGio) {
                throw new AppException(ErrorCode.SOLUONG_SANPHAM_KHONG_DU);
            }

            sanPhamChiTiet.setSoLuong(soLuongTonKho - soLuongTrongGio);
            updatedProducts.add(sanPhamChiTiet);
        }

        sanPhamChiTietRepository.saveAll(updatedProducts);
    }


    private Voucher checkVourcherCanUse(Long idVoucher) {
        Voucher voucher = voucherRepository.findByIdAndTrangThai(idVoucher).orElse(null);
        if (voucher != null) {
            voucher.setSoLuong(voucher.getSoLuong() - 1);
            voucherRepository.saveAndFlush(voucher);
            return voucher;
        }
        return voucher;
    }

}
