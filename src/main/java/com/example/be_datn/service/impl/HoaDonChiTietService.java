package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.HoaDonChiTietRequest;
import com.example.be_datn.dto.Request.HoaDonChiTietUpdateRequest;
import com.example.be_datn.dto.Response.HoaDonCTResponse;
import com.example.be_datn.entity.HoaDon;
import com.example.be_datn.entity.HoaDonCT;
import com.example.be_datn.entity.SanPhamChiTiet;
import com.example.be_datn.entity.StatusPayment;
import com.example.be_datn.entity.Voucher;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.HoaDonChiTietRepository;
import com.example.be_datn.repository.HoaDonRepository;
import com.example.be_datn.repository.SanPhamChiTietRepository;
import com.example.be_datn.repository.VoucherRepository;
import com.example.be_datn.service.IHoaDonChiTietService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HoaDonChiTietService implements IHoaDonChiTietService {
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final HoaDonRepository hoaDonRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final SanPhamChiTietService sanPhamChiTietService;
    private final VoucherRepository voucherRepository;




    @Override
    public String create(HoaDonChiTietRequest request) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(request.getIdSanPhamChiTiet()).get();
        HoaDon hoaDon = hoaDonRepository.findById(request.getIdHoaDon()).get();
        Optional<HoaDonCT> hoaDonChiTiet = hoaDonChiTietRepository.findByHoaDonAndSanPhamChiTiet(hoaDon, sanPhamChiTiet);

        if (hoaDonChiTiet.isEmpty()) {
            if (request.getSoLuong() > sanPhamChiTiet.getSoLuong() || request.getSoLuong() < 0) {
                throw new RuntimeException("Invalid quantity");
            }
            HoaDonCT hoaDonChiTiet1 = hoaDonChiTietRepository.save(
                    HoaDonCT.builder()
                            .hoaDon(hoaDon)
                            .sanPhamChiTiet(sanPhamChiTiet)
                            .soLuong(request.getSoLuong())
                            .giaTien(sanPhamChiTiet.getGiaBanSauKhiGiam())
                            .build()
            );
            updateHoaDon(hoaDon.getId());
            sanPhamChiTietService.updateSoLuongSanPhamChiTiet(sanPhamChiTiet.getId(), hoaDonChiTiet1.getSoLuong(), "minus");
        } else {
            HoaDonCT existingChiTiet = hoaDonChiTiet.get();
            Integer currentSoLuong = existingChiTiet.getSoLuong() != null ? existingChiTiet.getSoLuong() : 0;
            Integer newSoLuong = currentSoLuong + request.getSoLuong();

            if (newSoLuong > sanPhamChiTiet.getSoLuong()) {
                throw new RuntimeException("Not enough stock");
            }
            HoaDonCT hoaDonChiTiet1 = hoaDonChiTietRepository.save(
                    HoaDonCT.builder()
                            .id(existingChiTiet.getId())
                            .hoaDon(hoaDon)
                            .sanPhamChiTiet(sanPhamChiTiet)
                            .soLuong(newSoLuong)
                            .giaTien(sanPhamChiTiet.getGiaBanSauKhiGiam())
                            .build()
            );
            updateHoaDon(hoaDon.getId());
            sanPhamChiTietService.updateSoLuongSanPhamChiTiet(sanPhamChiTiet.getId(), request.getSoLuong(), "minus");
        }
        return "Success";
    }


    @Override
    @Transactional
    public String deleteHoaDonChiTiet(Long id) {
        HoaDonCT hoaDonChiTiet = hoaDonChiTietRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.HOA_DON_CT_NOT_FOUND));

        hoaDonChiTietRepository.deleteById(id);
        updateHoaDon(hoaDonChiTiet.getHoaDon().getId());
        sanPhamChiTietService.updateSoLuongSanPhamChiTiet(hoaDonChiTiet.getSanPhamChiTiet().getId(), hoaDonChiTiet.getSoLuong(), "plus");
        return "Success";
    }

    @Override
    public HoaDonCTResponse update(HoaDonChiTietUpdateRequest request, Long id) {
        if (request == null || id == null) {
            throw new IllegalArgumentException("Request hoặc Id không được null");
        }

        HoaDonCT hoaDonChiTiet = hoaDonChiTietRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hóa đơn chi tiết"));
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(request.getIdSanPhamChiTiet())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm chi tiết"));
        HoaDon hoaDon = hoaDonRepository.findById(request.getIdHoaDon())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hóa đơn"));


        Integer currentQuantity = hoaDonChiTiet.getSoLuong();
        Integer requestedQuantity = request.getSoLuong();
        Integer updatedQuantity;

        switch (request.getMethod().toUpperCase()) {
            case "PLUS":
                if (requestedQuantity + currentQuantity > sanPhamChiTiet.getSoLuong()) {
                    throw new IllegalArgumentException("Số lượng vượt quá tồn kho");
                }
                updatedQuantity = currentQuantity + requestedQuantity;
                break;

            case "MINUS":
                if (currentQuantity - requestedQuantity < 0) {
                    throw new IllegalArgumentException("Số lượng không hợp lệ");
                }
                updatedQuantity = currentQuantity - requestedQuantity;
                break;

            default:
                throw new IllegalArgumentException("Phương thức không hợp lệ");
        }
        hoaDonChiTiet.setSoLuong(updatedQuantity);
        hoaDonChiTietRepository.save(hoaDonChiTiet);

        updateHoaDon(hoaDon.getId());

        int stockChange = request.getMethod().equalsIgnoreCase("minus") ? requestedQuantity : -requestedQuantity;
        sanPhamChiTietService.updateSoLuongSanPhamChiTiet(sanPhamChiTiet.getId(), Math.abs(stockChange),
                stockChange > 0 ? "plus" : "minus");

        HoaDonCTResponse response = new HoaDonCTResponse();
        response.setId(hoaDonChiTiet.getId());
        response.setTenSanPhamChiTiet(sanPhamChiTiet.getSanPham().getTenSanPham());
        response.setSoLuong(updatedQuantity);
        response.setIdSanPhamChiTiet(sanPhamChiTiet.getId());

        return response;
    }

    @Override
    public Page<HoaDonCTResponse> getAllHdct(Pageable pageable, Long id) {


        return null;
    }

    @Override
    public List<HoaDonCTResponse> getAllHdctByIdHoaDon(Long hoaDonId) {
        List<HoaDonCT> list =  hoaDonChiTietRepository.getAllByHoaDon_Id(hoaDonId);
        List<HoaDonCTResponse> responseList = new ArrayList<>();
        for(HoaDonCT hoaDonCT : list){
            HoaDonCTResponse response = new HoaDonCTResponse();
            response.setId(hoaDonCT.getId());
            response.setIdGioHang(hoaDonCT.getHoaDon().getId());
            response.setIdSanPhamChiTiet(hoaDonCT.getSanPhamChiTiet().getId());
            response.setTenSanPhamChiTiet(hoaDonCT.getSanPhamChiTiet().getSanPham().getTenSanPham());
            response.setSoLuong(hoaDonCT.getSoLuong());
            response.setGiaBan(hoaDonCT.getGiaTien());
            response.setTongTien(hoaDonCT.getSoLuong() * hoaDonCT.getGiaTien());
            response.setHinhAnhList(hoaDonCT.getSanPhamChiTiet().getHinhAnhList());
            responseList.add(response);
        }
        return responseList;
    }


    @Transactional
    @Modifying
    public String updateHoaDon(Long id) {
        Double tongTien = 0.0;

        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.HOA_DON_NOT_FOUND));

        // Lấy danh sách chi tiết hóa đơn
        List<HoaDonCTResponse> list = getAllHdctByIdHoaDon(id);

        // Tính tổng tiền từ các chi tiết hóa đơn
        if (list != null && !list.isEmpty()) {
            for (HoaDonCTResponse response : list) {
                double tongTienChiTiet = response.getGiaBan() * response.getSoLuong();
                tongTien += tongTienChiTiet;
            }
        }

        // Cập nhật trạng thái của hóa đơn
        if (list.size() > 0) {
            hoaDon.setTrangThai(String.valueOf(StatusPayment.WAITING));
        } else {
            hoaDon.setTrangThai(String.valueOf(StatusPayment.PENDING));
            hoaDon.setVoucher(null);
            hoaDon.setSoTienGiam(0.0);
            hoaDon.setTienSauGiam(0.0);
        }

        hoaDon.setTongTien(tongTien);
        hoaDonRepository.save(hoaDon);
        List<Voucher> voucherList = voucherRepository.findAll();
        selectTheBestVoucherAndApply(hoaDon.getId(), voucherList);

        return "success";
    }

    public String selectTheBestVoucherAndApply(Long idHoaDon, List<Voucher> voucherList) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new AppException(ErrorCode.HOA_DON_NOT_FOUND));

        Voucher bestVoucher = voucherList.stream()
                .filter(voucher -> hoaDon.getTongTien() >= voucher.getGiaTriDonHangToiThieu())
                .sorted(Comparator.comparingDouble((Voucher voucher) -> calculateDiscount(hoaDon.getTongTien(), voucher))
                        .reversed())
                .findFirst()
                .orElse(null);

        if (bestVoucher != null) {
            double discount = calculateDiscount(hoaDon.getTongTien(), bestVoucher);
            hoaDon.setVoucher(bestVoucher);
            hoaDon.setSoTienGiam(discount);
            hoaDon.setTienSauGiam(hoaDon.getTongTien() - discount);
            hoaDonRepository.save(hoaDon);
        } else {
            hoaDon.setVoucher(null);
            hoaDon.setSoTienGiam(0.0);
            hoaDon.setTienSauGiam(hoaDon.getTongTien());
            hoaDonRepository.save(hoaDon);
        }

        return "Success";
    }

    private double calculateDiscount(double totalAmount, Voucher voucher) {
        double discount;

        if (voucher.getHinhThucGiam().equalsIgnoreCase("%")) {
            discount = totalAmount * (voucher.getGiaTriGiam() / 100);
        } else {
            discount = voucher.getGiaTriGiam();
        }

        // Check if the discount exceeds the maximum allowable discount (giaTriGiamToiDa)
        if (voucher.getGiaTriGiamToiDa() != null && discount > voucher.getGiaTriGiamToiDa()) {
            return voucher.getGiaTriGiamToiDa(); // Apply the max discount if exceeded
        }

        return discount;
    }


}
