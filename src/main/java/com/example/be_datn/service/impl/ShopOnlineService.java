package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.HoaDonRequest;
import com.example.be_datn.dto.Response.*;
import com.example.be_datn.entity.*;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.mapper.HoaDonChiTietMapper;
import com.example.be_datn.mapper.HoaDonMapper;
import com.example.be_datn.mapper.LichSuHoaDonMapper;
import com.example.be_datn.repository.*;
import com.example.be_datn.service.IShopOnlineService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
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

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 10;
    private final GioHangChiTietRepository gioHangChiTietRepository;

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
            totalPrice += gioHangChiTietResponse.getSanPhamChiTietResponse().getGiaBan() * gioHangChiTietResponse.getSoLuong();
        }
        return AboutProductShopOn.builder()
                .idGioHang(gioHang.getId())
                .gioHangChiTietList(gioHangChiTietResponses)
                .totalPrice(totalPrice)
                .build();
    }

    @Override
    public HoaDonResponse checkout(HoaDonRequest hoaDonRequest) {
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

        List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietRepository.findByGioHang_Id(hoaDonRequest.getIdGioHang());
        gioHangChiTietList.stream()
                .map(gioHangChiTiet -> hoaDonChiTietMapper.toHoaDonCT(gioHangChiTiet, hoaDon.getId()))
                .forEach(hoaDonChiTietRepository::saveAndFlush);

        emailService.sendMailToUser("anhdeptrai7749@gmail.com", "3HST Shoes - Cảm ơn bạn đã đặt hàng tại 3HST Shoes", hoaDon.getMaHoaDon(), this.getInfoOrder(hoaDon.getMaHoaDon()));
        return hoaDonMapper.toHoaDonResponse(hoaDon);
    }

    @Override
    public InfoOrder getInfoOrder(String maHoaDon) {
        if (maHoaDon.isBlank()) {
            throw new AppException(ErrorCode.HOA_DON_NOT_FOUND);
        }
        HoaDon hoaDon = hoaDonRepository.findByMaHoaDon(maHoaDon);
        if(hoaDon == null){
            throw new AppException(ErrorCode.HOA_DON_NOT_FOUND);
        }
        List<HoaDonCT> hoaDonCTList = hoaDonChiTietRepository.findByHoaDon_Id(hoaDon.getId());
        if (hoaDonCTList.isEmpty()) {
            throw new AppException(ErrorCode.HOA_DON_CHI_TIET_NOT_FOUND_LIST);
        }
        return InfoOrder.builder()
                .hoaDonResponse(hoaDonMapper.toHoaDonResponse(hoaDon))
                .hoaDonChiTietResponse(hoaDonChiTietMapper.toListResponse(hoaDonCTList))
                .build();
    }

    @Override
    public List<HoaDonResponse> getAllOrderByStatus(String trangThai) {
        List<HoaDon> hoaDons = hoaDonRepository.findByTrangThai(trangThai);
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
        return DetailHistoryBillResponse.builder()
                .hoaDonResponse(hoaDonResponse)
                .lichSuHoaDonResponses(lichSuHoaDonResponses)
                .build();
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
}
