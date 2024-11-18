package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.HoaDonChiTietRequest;
import com.example.be_datn.dto.Request.HoaDonChiTietUpdateRequest;
import com.example.be_datn.dto.Response.HoaDonChiTietResponse;
import com.example.be_datn.dto.Response.HoaDonResponse;
import com.example.be_datn.entity.HoaDon;
import com.example.be_datn.entity.HoaDonChiTiet;
import com.example.be_datn.entity.SanPhamChiTiet;
import com.example.be_datn.repository.HoaDonChiTietRepository;
import com.example.be_datn.repository.HoaDonRepository;
import com.example.be_datn.repository.SanPhamChiTietRepository;
import com.example.be_datn.service.IHoaDonChiTietService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HoaDonChiTietService implements IHoaDonChiTietService {
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final HoaDonRepository hoaDonRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final SanPhamChiTietService sanPhamChiTietService;


    @Override
    public Page<HoaDonChiTietResponse> getAllHoaDonChitiet(Pageable pageable) {
        return hoaDonChiTietRepository.getAllHdct(pageable);
    }

    @Override
    public List<HoaDonChiTietResponse> getHoaDonChiTietByHoaDonId(Long hoaDonId) {
        return hoaDonChiTietRepository.getAllHdctByIdHoaDon(hoaDonId);
    }


    @Override
    public String create(HoaDonChiTietRequest request) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(request.getIdSanPhamChiTiet()).get();
        HoaDon hoaDon = hoaDonRepository.findById(request.getIdHoaDon()).get();
        Optional<HoaDonChiTiet> hoaDonChiTiet = hoaDonChiTietRepository.findByHoaDonAndSanPhamChiTiet(hoaDon, sanPhamChiTiet);

        if (hoaDonChiTiet.isEmpty()) {
            if (request.getSoLuong() > sanPhamChiTiet.getSoLuong() || request.getSoLuong() < 0) {
                throw new RuntimeException("Invalid quantity");
            }
            HoaDonChiTiet hoaDonChiTiet1 = hoaDonChiTietRepository.save(
                    HoaDonChiTiet.builder()
                            .hoaDon(hoaDon)
                            .sanPhamChiTiet(sanPhamChiTiet)
                            .soLuong(request.getSoLuong())
                            .tongTien(request.getSoLuong() * sanPhamChiTiet.getGiaBan())
                            .giaTien(sanPhamChiTiet.getGiaBan())
                            .build()
            );
            updateHoaDon(hoaDon.getId());
            sanPhamChiTietService.updateSoLuongSanPhamChiTiet(sanPhamChiTiet.getId(), hoaDonChiTiet1.getSoLuong(), "minus");
        } else {
            HoaDonChiTiet existingChiTiet = hoaDonChiTiet.get();
            Integer currentSoLuong = existingChiTiet.getSoLuong() != null ? existingChiTiet.getSoLuong() : 0;
            Integer newSoLuong = currentSoLuong + request.getSoLuong();

            if (newSoLuong > sanPhamChiTiet.getSoLuong()) {
                throw new RuntimeException("Not enough stock");
            }

            Double currentTongTien = existingChiTiet.getTongTien() != null ? existingChiTiet.getTongTien() : 0.0;
            Double newTongTien = currentTongTien + (request.getSoLuong() * sanPhamChiTiet.getGiaBan());

            HoaDonChiTiet hoaDonChiTiet1 = hoaDonChiTietRepository.save(
                    HoaDonChiTiet.builder()
                            .id(existingChiTiet.getId())
                            .hoaDon(hoaDon)
                            .sanPhamChiTiet(sanPhamChiTiet)
                            .soLuong(newSoLuong)
                            .giaTien(sanPhamChiTiet.getGiaBan())
                            .tongTien(newTongTien)
                            .build()
            );
            updateHoaDon(hoaDon.getId());
            sanPhamChiTietService.updateSoLuongSanPhamChiTiet(sanPhamChiTiet.getId(), request.getSoLuong(), "minus");
        }
        return "Success";
    }


    @Override
    @Transactional
    public HoaDon deleteHoaDonChiTiet(Long id) {
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(id).get();
        hoaDonChiTietRepository.deleteById(id);
        updateHoaDon(hoaDonChiTiet.getHoaDon().getId());
        sanPhamChiTietService.updateSoLuongSanPhamChiTiet(hoaDonChiTiet.getSanPhamChiTiet().getId(), hoaDonChiTiet.getSoLuong(), "plus");
        return hoaDonRepository.findById(hoaDonChiTiet.getHoaDon().getId()).get();
    }

    @Override
//    public HoaDonChiTietResponse update(HoaDonChiTietUpdateRequest request, Long id) {
//        Integer tongSoLuong;
//        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(id).get();
//        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(request.getIdSanPhamChiTiet()).orElseThrow();
//        HoaDon hoaDon = hoaDonRepository.findById(request.getIdHoaDon()).get();
//        if (request.getMethod().equalsIgnoreCase("plus")) {
//            if (request.getSoLuong() > sanPhamChiTiet.getSoLuong()) {
//                throw new RuntimeException();
//            }
//            tongSoLuong = request.getSoLuong() + hoaDonChiTiet.getSoLuong();
//        } else {
//            tongSoLuong = request.getSoLuong() - hoaDonChiTiet.getSoLuong();
//        }
//        hoaDonChiTiet.setSoLuong(tongSoLuong);
//        hoaDonChiTiet.setTongTien(sanPhamChiTiet.getGiaBan() * tongSoLuong);
//        hoaDonChiTietRepository.save(hoaDonChiTiet);
//        updateHoaDon(hoaDon.getId());
//        if (request.getMethod().equalsIgnoreCase("minus")) {
//            sanPhamChiTietService.updateSoLuongSanPhamChiTiet(sanPhamChiTiet.getId(), request.getSoLuong(), "plus");
//        } else {
//            sanPhamChiTietService.updateSoLuongSanPhamChiTiet(sanPhamChiTiet.getId(), request.getSoLuong(), "minus");
//        }
//        return null;
//    }
    public HoaDonChiTietResponse update(HoaDonChiTietUpdateRequest request, Long id) {
        // Lấy thông tin cần thiết
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn chi tiết"));
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(request.getIdSanPhamChiTiet())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết"));
        HoaDon hoaDon = hoaDonRepository.findById(request.getIdHoaDon())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        // Xử lý logic cộng/trừ
        Integer currentQuantity = hoaDonChiTiet.getSoLuong();
        Integer requestedQuantity = request.getSoLuong();
        Integer updatedQuantity;

        if (request.getMethod().equalsIgnoreCase("plus")) {
            // Kiểm tra số lượng tồn kho
            if (requestedQuantity + currentQuantity > sanPhamChiTiet.getSoLuong()) {
                throw new RuntimeException("Số lượng vượt quá tồn kho");
            }
            updatedQuantity = currentQuantity + requestedQuantity;
        } else if (request.getMethod().equalsIgnoreCase("minus")) {
            // Kiểm tra số lượng hợp lệ
            if (currentQuantity - requestedQuantity < 0) {
                throw new RuntimeException("Số lượng không hợp lệ");
            }
            updatedQuantity = currentQuantity - requestedQuantity;
        } else {
            throw new IllegalArgumentException("Phương thức không hợp lệ");
        }

        // Cập nhật hóa đơn chi tiết
        hoaDonChiTiet.setSoLuong(updatedQuantity);
        hoaDonChiTiet.setTongTien(sanPhamChiTiet.getGiaBan() * updatedQuantity);
        hoaDonChiTietRepository.save(hoaDonChiTiet);

        // Cập nhật hóa đơn tổng
        updateHoaDon(hoaDon.getId());

        // Cập nhật số lượng sản phẩm tồn kho
        int stockChange = request.getMethod().equalsIgnoreCase("minus") ? requestedQuantity : -requestedQuantity;
        sanPhamChiTietService.updateSoLuongSanPhamChiTiet(sanPhamChiTiet.getId(), Math.abs(stockChange),
                stockChange > 0 ? "plus" : "minus");

        // Trả về response
        HoaDonChiTietResponse response = new HoaDonChiTietResponse();
        response.setId(hoaDonChiTiet.getId());
        response.setTenSanPhamChiTiet(sanPhamChiTiet.getSanPham().getTenSanPham());
        response.setSoLuong(updatedQuantity);
        response.setIdSanPhamChiTiet(sanPhamChiTiet.getId());
        response.setTongTien(hoaDonChiTiet.getTongTien());
        return response;
    }


    @Modifying
    public String updateHoaDon(Long id) {
        Double tongTien = 0.0;
        Integer soLuong = 0;
        List<HoaDonChiTietResponse> list = getHoaDonChiTietByHoaDonId(id);
        if (list == null || list.isEmpty()) {
            tongTien = 0.0;
            soLuong = 0;
        }
        for (HoaDonChiTietResponse response : list) {
            tongTien += (response.getTongTien() != null ? response.getTongTien() : 0.0);
            soLuong += response.getSoLuong();
        }
        hoaDonRepository.updateHoaDon(tongTien, soLuong, id);
        return "success";
    }

}
