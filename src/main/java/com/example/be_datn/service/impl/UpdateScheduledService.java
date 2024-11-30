package com.example.be_datn.service.impl;

import com.example.be_datn.entity.*;
import com.example.be_datn.repository.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UpdateScheduledService {
    GioHangChiTietRepository gioHangChiTietRepository;
    Sale_ChiTietRepository sale_ChiTietRepository;
    SaleRepository saleRepository;
    SanPhamChiTietRepository sanPhamChiTietRepository;
    VoucherRepository voucherRepository;

    @Scheduled(fixedRate = 60000)
    public void updateGiaGioHang() {

        List<GioHangChiTiet> getAll = gioHangChiTietRepository.findAll();
        for (GioHangChiTiet gioHangChiTiet : getAll) {
            SaleCt saleCt = sale_ChiTietRepository.findMostRecentByIdSanPhamCtAndIdSale(gioHangChiTiet.getSanPhamChiTiet().getId());
            if (saleCt == null) {
                gioHangChiTiet.setGiaTien(gioHangChiTiet.getSanPhamChiTiet().getGiaBan());
                gioHangChiTiet.setThoiGianGiamGia(null);
                gioHangChiTietRepository.save(gioHangChiTiet);
            }else {
                gioHangChiTiet.setGiaTien(gioHangChiTiet.getSanPhamChiTiet().getGiaBanSauKhiGiam());
                gioHangChiTiet.setThoiGianGiamGia(saleCt.getSale().getThoiGianKetThuc());
                gioHangChiTietRepository.save(gioHangChiTiet);
            }
            System.out.println("update gia tien");
        }

    }
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateSale() {
        LocalDateTime now = LocalDateTime.now();
        List<Sale> sales = saleRepository.findByThoiGianKetThucBefore(now);
        for (Sale sale : sales) {
            sale.setTrangThai(0);
            List<SaleCt> saleCts = sale.getSaleCts();
            if (saleCts == null) {
                saleRepository.save(sale);
                continue;
            }
            for (SaleCt saleCt : saleCts) {
                Optional<SanPhamChiTiet> optionalSanPhamChiTiet = sanPhamChiTietRepository.findById(saleCt.getIdSanPhamCt());
                if (optionalSanPhamChiTiet.isPresent()) {
                    SanPhamChiTiet sanPhamChiTiet = optionalSanPhamChiTiet.get();

                    // Kiểm tra sản phẩm có đang tham gia vào một đợt sale khác không
                    boolean isProductOnSale = saleRepository.existsBySaleCtsIdSanPhamCtAndThoiGianKetThucAfter(sanPhamChiTiet.getId(), now);

                    // Nếu sản phẩm không còn sale nào đang áp dụng, cập nhật giá
                    if (!isProductOnSale) {
                        sanPhamChiTiet.setGiaBanSauKhiGiam(sanPhamChiTiet.getGiaBan());
                        sanPhamChiTietRepository.save(sanPhamChiTiet);
                    }
                }

                // Cập nhật trạng thái của SaleCt
                saleCt.setTrangThai(0);
                sale_ChiTietRepository.save(saleCt);
            }
            // Lưu lại Sale sau khi xử lý
            saleRepository.save(sale);
        }
        System.out.println("update sale");
    }
     @Scheduled(fixedRate = 60000)
    public void updateVoucher(){
        LocalDateTime now = LocalDateTime.now();
        List<Voucher> vouchers = voucherRepository.findByNgayKetThucBefore(now);
        for (Voucher voucher : vouchers) {
            voucher.setTrangThai(0);
            voucherRepository.save(voucher);
        }
        System.out.println("update voucher");
    }

}
