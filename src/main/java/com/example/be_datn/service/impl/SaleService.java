package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.SaleRequest;
import com.example.be_datn.dto.Response.SaleResponse;
import com.example.be_datn.entity.Sale;
import com.example.be_datn.entity.SaleCt;
import com.example.be_datn.entity.SanPhamChiTiet;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.SaleRepository;
import com.example.be_datn.repository.Sale_ChiTietRepository;
import com.example.be_datn.repository.SanPhamChiTietRepository;
import com.example.be_datn.service.ISaleService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SaleService implements ISaleService {
    SaleRepository saleRepository;
    Sale_ChiTietRepository sale_ChiTietRepository;
    SanPhamChiTietRepository sanPhamChiTietRepository;
//    @Override
//    public Page<SaleResponse> getAll(String tenChienDich, String ngayBatDau, String ngayKetthuc, int trangThai, Pageable pageable) {
//        return saleRepository.findAllBy(tenChienDich,ngayBatDau,ngayKetthuc, trangThai, pageable).map(SaleResponse::fromSale);
//    }


//    @Override
//    public Page<SaleResponse> getAll(Pageable pageable) {
//        return saleRepository.findAllBy(pageable).map(SaleResponse::fromSale);
//    }
    @Override
    public Page<SaleResponse> getAll(String tenChienDich, java.time.LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, Integer trangThai, Pageable pageable) {
        return saleRepository.findAllByFilter(tenChienDich ,ngayBatDau,ngayKetThuc,trangThai,pageable).map(SaleResponse::fromSale);
    }


    @Override
    public SaleResponse getById(Long id) {
        return saleRepository.findById(id).map(SaleResponse::fromSale).orElse(null);
    }

    @Override
    public SaleResponse create(SaleRequest saleRequest) {
        if(saleRepository.existsByTenChienDich(saleRequest.getTenChienDich())){
           throw new AppException(ErrorCode.TEN_SALE_INVALID);
        }
        if(saleRequest.getThoiGianKetThuc().isBefore(saleRequest.getThoiGianBatDau())||
                saleRequest.getThoiGianKetThuc().isEqual(saleRequest.getThoiGianBatDau())){
            throw new AppException(ErrorCode.THOI_GIAN_INVALID);
        }

        Sale sale = buildSale(saleRequest);
        if(saleRequest.getThoiGianBatDau().isAfter(LocalDateTime.now())){
            sale.setTrangThai(0);
        }else {
            sale.setTrangThai(saleRequest.getTrangThai());
        }
        SaleResponse saleResponse = SaleResponse.fromSale(saleRepository.save(sale));
        Sale saleSave = saleRepository.getReferenceById(saleResponse.getId());
        List<SaleCt> saleCts = new ArrayList<>();
        for (int i = 0; i < saleRequest.getIdSanPhamChiTiet().size(); i++) {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(saleRequest.getIdSanPhamChiTiet().get(i)).orElse(null);
            if(sanPhamChiTiet == null){
                throw new AppException(ErrorCode.SANPHAMCHITIET_NOT_FOUND);
            }
            sanPhamChiTiet.setGiaBanSauKhiGiam((Double) (sanPhamChiTiet.getGiaBan()*(100-saleRequest.getGiaTriGiam())/100));
            sanPhamChiTietRepository.save(sanPhamChiTiet);
            SaleCt saleCt = new SaleCt();
            saleCt.setIdSanPhamCt(saleRequest.getIdSanPhamChiTiet().get(i));
            saleCt.setSale(saleSave);
            saleCt.setGiaTriGiam(saleRequest.getGiaTriGiam());
            saleCt.setHinhThucGiam(saleRequest.getHinhThucGiam());
            saleCt.setTienGiam((float) (sanPhamChiTiet.getGiaBan()*saleRequest.getGiaTriGiam()/100));
            saleCt.setTrangThai(1);
            sale_ChiTietRepository.save(saleCt);
            saleCts.add(saleCt);
        }
        saleResponse.setSaleCts(saleCts);
        return saleResponse;

    }

    @Override
    public SaleResponse update(SaleRequest saleRequest, Long id) {
        Sale sale = saleRepository.findById(id).orElse(null);
        if(sale == null){
            throw new AppException(ErrorCode.Sale_NOT_FOUND);
        }
        if(saleRequest.getThoiGianKetThuc().isBefore(saleRequest.getThoiGianBatDau())||
        saleRequest.getThoiGianKetThuc().isEqual(saleRequest.getThoiGianBatDau())){
            throw new AppException(ErrorCode.THOI_GIAN_INVALID);
        }
        if(saleRequest.getThoiGianBatDau().isAfter(LocalDateTime.now())){
            sale.setTrangThai(0);
        }else {
            sale.setTrangThai(saleRequest.getTrangThai());
        }
        sale.setTenChienDich(saleRequest.getTenChienDich());
        sale.setHinhThucGiam(saleRequest.getHinhThucGiam());
        sale.setGiaTriGiam(saleRequest.getGiaTriGiam());
        sale.setThoiGianBatDau(saleRequest.getThoiGianBatDau());
        sale.setThoiGianKetThuc(saleRequest.getThoiGianKetThuc());

        SaleResponse saleResponse = SaleResponse.fromSale(saleRepository.save(sale));
        Sale saleSave = saleRepository.getReferenceById(saleResponse.getId());
        List<SaleCt> saleCts = sale.getSaleCts();
        List<Long> idSanPhamChiTietNew = saleRequest.getIdSanPhamChiTiet();
        List<Long> idSanPhamChiTietOld = saleCts!=null ? saleCts.stream().map(SaleCt::getIdSanPhamCt).toList():new ArrayList<>();
        for (SaleCt saleCt : saleCts) {
            saleCt.setGiaTriGiam(saleRequest.getGiaTriGiam());
            if(sale.getTrangThai()==1){
                SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(saleCt.getIdSanPhamCt()).orElse(null);
                if(sanPhamChiTiet == null){
                    throw new AppException(ErrorCode.SANPHAMCHITIET_NOT_FOUND);
                }
                sanPhamChiTiet.setGiaBanSauKhiGiam((Double) (sanPhamChiTiet.getGiaBan()*(100-saleRequest.getGiaTriGiam())/100));
                sanPhamChiTietRepository.save(sanPhamChiTiet);
            }else {
                SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(saleCt.getIdSanPhamCt()).orElse(null);
                if(sanPhamChiTiet == null){
                    throw new AppException(ErrorCode.SANPHAMCHITIET_NOT_FOUND);
                }
                SaleCt saleCt1 = sale_ChiTietRepository.findMostRecentByIdSanPhamCtAndIdSale(sanPhamChiTiet.getId());
                if (saleCt1 == null){
                    sanPhamChiTiet.setGiaBanSauKhiGiam(sanPhamChiTiet.getGiaBan());
                    sanPhamChiTietRepository.save(sanPhamChiTiet);
                }else {
                    sanPhamChiTiet.setGiaBanSauKhiGiam((Double) (sanPhamChiTiet.getGiaBan()*(100-saleCt1.getGiaTriGiam())/100));
                    sanPhamChiTietRepository.save(sanPhamChiTiet);
                }

            }
            sale_ChiTietRepository.save(saleCt);
        }
        for (Long idSanPhamChiTiet : idSanPhamChiTietOld) {
            if(!idSanPhamChiTietNew.contains(idSanPhamChiTiet)){
                SaleCt saleCt = sale_ChiTietRepository.findByIdSanPhamCtAndIdSale(idSanPhamChiTiet, id);
//                if(saleCt != null){
//
//                    sale_ChiTietRepository.delete(saleCt);
//                }
                if (saleCt != null) {
                    // Xóa SaleCt
                    sale_ChiTietRepository.delete(saleCt);

                    // Lấy sản phẩm chi tiết
                    SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(idSanPhamChiTiet).orElse(null);
                    if (sanPhamChiTiet == null) {
                        throw new AppException(ErrorCode.SANPHAMCHITIET_NOT_FOUND);
                    }

                    // Kiểm tra xem có chiến dịch giảm giá nào khác cho sản phẩm không
                    SaleCt mostRecentSaleCt = sale_ChiTietRepository.findMostRecentByIdSanPhamCtAndIdSale(idSanPhamChiTiet);
                    if (mostRecentSaleCt == null) {
                        // Không còn chiến dịch giảm giá, khôi phục giá gốc
                        sanPhamChiTiet.setGiaBanSauKhiGiam(sanPhamChiTiet.getGiaBan());
                    } else {
                        // Còn chiến dịch khác, tính lại giá sau khi giảm
                        sanPhamChiTiet.setGiaBanSauKhiGiam(
                                (Double) (sanPhamChiTiet.getGiaBan() * (100 - mostRecentSaleCt.getGiaTriGiam()) / 100)
                        );
                    }
                    sanPhamChiTietRepository.save(sanPhamChiTiet);
                }
            }
        }
        for (Long idSanPhamChiTiet : idSanPhamChiTietNew) {
            if(!idSanPhamChiTietOld.contains(idSanPhamChiTiet)){
                SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(idSanPhamChiTiet).orElse(null);
                if(sanPhamChiTiet == null){
                    throw new AppException(ErrorCode.SANPHAMCHITIET_NOT_FOUND);
                }
                SaleCt saleCt = sale_ChiTietRepository.findByIdSanPhamCtAndIdSale(idSanPhamChiTiet, id);
                if(sale.getTrangThai() == 1){
                    sanPhamChiTiet.setGiaBanSauKhiGiam((Double) (sanPhamChiTiet.getGiaBan()*(100-saleRequest.getGiaTriGiam())/100));
                    sanPhamChiTietRepository.save(sanPhamChiTiet);
                }else {
                    sanPhamChiTiet.setGiaBanSauKhiGiam(sanPhamChiTiet.getGiaBan());
                    sanPhamChiTietRepository.save(sanPhamChiTiet);
                }

                if(saleCt == null){
                    saleCt = new SaleCt();
                    saleCt.setIdSanPhamCt(idSanPhamChiTiet);
                    saleCt.setSale(saleSave);
                    saleCt.setGiaTriGiam(saleRequest.getGiaTriGiam());
                    saleCt.setHinhThucGiam(saleRequest.getHinhThucGiam());
                    saleCt.setTienGiam((float) (sanPhamChiTiet.getGiaBan()*saleRequest.getGiaTriGiam()/100));
                    saleCt.setTrangThai(1);
                    sale_ChiTietRepository.save(saleCt);
                    saleCts.add(saleCt);
                }
            }
        }
        saleResponse.setSaleCts(saleCts);
        return saleResponse;
    }

    @Override
    public String delete(Long id) {
        return "";
    }

    @Override
    public SaleResponse updateStatus(Long id, int status) {
        Sale sale = saleRepository.findById(id).orElse(null);
        if(sale == null){
            throw new AppException(ErrorCode.Sale_NOT_FOUND);
        }
        sale.setTrangThai(status);
        return SaleResponse.fromSale(saleRepository.save(sale));

    }

    @Override
    public List<String> getAllTenChienDich() {
        return saleRepository.getALLTenChienDich();
    }

    public Sale buildSale(SaleRequest saleRequest) {
        Sale sale = new Sale();
        sale.setTenChienDich(saleRequest.getTenChienDich());
        sale.setHinhThucGiam(saleRequest.getHinhThucGiam());
        sale.setGiaTriGiam(saleRequest.getGiaTriGiam());
        sale.setThoiGianBatDau(saleRequest.getThoiGianBatDau());
        sale.setThoiGianKetThuc(saleRequest.getThoiGianKetThuc());
        sale.setTrangThai(saleRequest.getTrangThai());
        return sale;
    }
}
