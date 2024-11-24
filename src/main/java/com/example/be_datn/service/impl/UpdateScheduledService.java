package com.example.be_datn.service.impl;

import com.example.be_datn.entity.GioHangChiTiet;
import com.example.be_datn.entity.Sale;
import com.example.be_datn.entity.SaleCt;
import com.example.be_datn.repository.GioHangChiTietRepository;
import com.example.be_datn.repository.SaleRepository;
import com.example.be_datn.repository.Sale_ChiTietRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UpdateScheduledService {
    GioHangChiTietRepository gioHangChiTietRepository;
    Sale_ChiTietRepository sale_ChiTietRepository;
    SaleRepository saleRepository;

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
                gioHangChiTiet.setGiaTien(gioHangChiTiet.getSanPhamChiTiet().getGiaBan()-saleCt.getTienGiam());
                gioHangChiTiet.setThoiGianGiamGia(saleCt.getSale().getThoiGianKetThuc());
                gioHangChiTietRepository.save(gioHangChiTiet);
            }
            System.out.println("update gia tien");
        }

    }
    @Scheduled(fixedRate = 60000)
    public void updateSale() {
        LocalDateTime now = LocalDateTime.now();
        List< Sale> sales = saleRepository.findByThoiGianKetThucBefore(now);
        for (Sale sale : sales) {
            sale.setTrangThai(0);
            saleRepository.save(sale);
        }
        System.out.println("update sale");
    }
}
