package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.SanPhamFilterRequest;
import com.example.be_datn.dto.Request.SanPhamRequest;
import com.example.be_datn.dto.Response.SanPhamCustumerResponse;
import com.example.be_datn.dto.Response.SanPhamResponse;
import com.example.be_datn.entity.HinhAnh;
import com.example.be_datn.entity.SaleCt;
import com.example.be_datn.entity.SanPham;
import com.example.be_datn.entity.SanPhamChiTiet;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.*;
import com.example.be_datn.service.ISanPhamService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SanPhamService implements ISanPhamService {
    SanPhamRepository sanPhamRepository;
    DanhMucRepository danhMucRepository;
    ThuongHieuRepository thuongHieuRepository;
    ChatLieuDeRepository chatLieuDeRepository;
    ChatLieuVaiRepository chatLieuVaiRepository;
    Sale_CTService sale_ctService;

    @Override
    public Page<SanPhamResponse> getAllPageable(Pageable pageable) {
        return sanPhamRepository.findAll(pageable).map(SanPhamResponse::fromSanPham);
    }

    @Override
    public Page<SanPham> getAllPageableCustumer(Pageable pageable) {
        return sanPhamRepository.getAllByFilterCustumer(pageable);
    }

    @Override
    public Page<SanPhamCustumerResponse> getAllPageableCustumerFilter(List<Long> danhMucs, List<Long> thuongHieus, List<Long> chatLieuDes, List<Long> chatLieuVai, String ten, Double min, Double max, String sortBy, Pageable pageable) {
        Specification<SanPham> spec = Specification.where(SanPhamSpecification.activeUsers())
                .and(SanPhamSpecification.byDanhMuc(danhMucs))
                .and(SanPhamSpecification.byChatLieuDe(chatLieuDes))
                .and(SanPhamSpecification.byChatLieuVai(chatLieuVai))
                .and(SanPhamSpecification.byThuongHieu(thuongHieus))
                .and(SanPhamSpecification.byNameSanPham(ten))
                .and(SanPhamSpecification.byMinPrice(min))
                .and(SanPhamSpecification.byMaxPrice(max));
        // Thêm sắp xếp
        if ("tuCaoDenThap".equals(sortBy)) {
            spec = spec.and(SanPhamSpecification.sortByPriceDesc());
        } else if ("tuThapDenCao".equals(sortBy)) {
            spec = spec.and(SanPhamSpecification.sortByPriceAsc());
        } else if ("moiNhat".equals(sortBy)) {
            spec = spec.and(SanPhamSpecification.sortByNewest());
        }

        Page<SanPham> sanPhams = sanPhamRepository.findAll(spec, pageable);
        Page<SanPhamCustumerResponse> sanPhamCustumerResponses = sanPhams.map(sanPham -> {


            List<Double> giaBan = sanPham.getSanPhamChiTietList().stream().map(SanPhamChiTiet::getGiaBanSauKhiGiam).toList();
            Double giaBanThapNhat = giaBan.stream().min(Double::compareTo).orElse(0.0);
            Double giaBanCaoNhat = giaBan.stream().max(Double::compareTo).orElse(0.0);
            String giaHienThi = giaBanThapNhat.equals(giaBanCaoNhat)
                    ? String.format("%,.0f VND", giaBanThapNhat)
                    : String.format("%,.0f - %,.0f VND", giaBanThapNhat, giaBanCaoNhat);

            String hinhAnh = sanPham.getSanPhamChiTietList()
                    .stream()
                    .flatMap(chiTiet -> chiTiet.getHinhAnhList().stream())
                    .map(HinhAnh::getUrl)
                    .findFirst()
                    .orElse(null);

            String phanTramGiamGia = "";
            for (SanPhamChiTiet sanPhamChiTiet : sanPham.getSanPhamChiTietList()) {
                SaleCt saleCts = sale_ctService.getSaleCtById(sanPhamChiTiet.getId());
                if (saleCts != null) {
//                    Double giaBanMoi = sanPhamChiTiet.getGiaBan() - saleCts.getTienGiam();
//                    giaBanThapNhat = giaBanMoi;
                    phanTramGiamGia = saleCts.getGiaTriGiam().toString();
//                    giaHienThi = String.format("%,.0f VND", giaBanMoi);
                    break;
                }
            }


            return new SanPhamCustumerResponse(
                    sanPham.getId(),
                    sanPham.getTenSanPham(),
                    giaBanThapNhat,
                    giaBanCaoNhat,
                    giaHienThi,
                    hinhAnh,
                    phanTramGiamGia,
                    0l,
                    sanPham.getSanPhamChiTietList().size()
            );
        });
        return sanPhamCustumerResponses;
    }

    @Override
    public List<SanPhamResponse> getAllByTenSanPhamContaning(String tenSanPham) {
        return sanPhamRepository.getAllByTenSanPhamContainingIgnoreCase(tenSanPham).stream().map(SanPhamResponse::fromSanPham).toList();
    }

    @Override
    public List<SanPhamCustumerResponse> getSanPhamByDanhMucID(Integer id) {

        // Lấy danh sách sản phẩm theo danh mục
        List<SanPham> sanPhams = sanPhamRepository.getSanPhamByDanhMucID(id);

        // Chuyển đổi sang danh sách SanPhamCustumerResponse
        List<SanPhamCustumerResponse> sanPhamCustumerResponses = sanPhams.stream().map(sanPham -> {
            // Lấy danh sách giá bán
            List<Double> giaBan = sanPham.getSanPhamChiTietList().stream()
                    .map(SanPhamChiTiet::getGiaBanSauKhiGiam)
                    .toList();

            // Tìm giá thấp nhất và cao nhất
            Double giaBanThapNhat = giaBan.stream().min(Double::compareTo).orElse(0.0);
            Double giaBanCaoNhat = giaBan.stream().max(Double::compareTo).orElse(0.0);

            // Hiển thị giá
            String giaHienThi = giaBanThapNhat.equals(giaBanCaoNhat)
                    ? String.format("%,.0f VND", giaBanThapNhat)
                    : String.format("%,.0f - %,.0f VND", giaBanThapNhat, giaBanCaoNhat);

            // Lấy hình ảnh đầu tiên
            String hinhAnh = sanPham.getSanPhamChiTietList().stream()
                    .flatMap(chiTiet -> chiTiet.getHinhAnhList().stream())
                    .map(HinhAnh::getUrl)
                    .findFirst()
                    .orElse(null);

            // Tìm giảm giá nếu có
            String phanTramGiamGia = "";
            for (SanPhamChiTiet sanPhamChiTiet : sanPham.getSanPhamChiTietList()) {
                SaleCt saleCts = sale_ctService.getSaleCtById(sanPhamChiTiet.getId());
                if (saleCts != null) {
//                    Double giaBanMoi = sanPhamChiTiet.getGiaBan() - saleCts.getTienGiam();
//                    giaBanThapNhat = Math.min(giaBanThapNhat, giaBanMoi);
                    phanTramGiamGia = saleCts.getGiaTriGiam().toString();
//                    giaHienThi = String.format("%,.0f VND", giaBanMoi);
                    break;
                }
            }

            // Tạo đối tượng SanPhamCustumerResponse
            return new SanPhamCustumerResponse(
                    sanPham.getId(),
                    sanPham.getTenSanPham(),
                    giaBanThapNhat,
                    giaBanCaoNhat,
                    giaHienThi,
                    hinhAnh,
                    phanTramGiamGia,
                    0l,
                    sanPham.getSanPhamChiTietList().size()
            );
        }).toList();

        return sanPhamCustumerResponses;
    }


    @Override
    public SanPhamResponse updateStatus(Long idSanPham, int status) {
        SanPham sanPham = sanPhamRepository.findById(idSanPham).orElseThrow(() -> new AppException(ErrorCode.SANPHAM_NOT_FOUND));
        sanPham.setTrangThai(status);
        return SanPhamResponse.fromSanPham(sanPhamRepository.save(sanPham));
    }

    @Override
    public Page<SanPhamResponse> getAllWithFilter(Long idDanhMuc, Long idThuongHieu, Long idChatLieuVai, Long idChatLieuDe, String tenSanPham, Pageable pageable) {
        return sanPhamRepository.getAllByFilter(idDanhMuc, idThuongHieu, idChatLieuVai, idChatLieuDe, "%" + tenSanPham + "%", pageable)
                .map(SanPhamResponse::fromSanPham);
    }

    @Override
    public SanPhamResponse getById(Long id) {
        return sanPhamRepository.findById(id).map(SanPhamResponse::fromSanPham).orElseThrow(() -> new AppException(ErrorCode.SANPHAM_NOT_FOUND));
    }

    @Override
    public SanPhamResponse create(SanPhamRequest sanPhamRequest) {
        if (sanPhamRepository.existsByTenSanPham(sanPhamRequest.getTenSanPham())) {
            throw new AppException(ErrorCode.TEN_SANPHAM_EXIST);
        }
        validateForeignKeys(sanPhamRequest);

        SanPham sanPham = buildSanPham(sanPhamRequest);
        return SanPhamResponse.fromSanPham(sanPhamRepository.save(sanPham));
    }

    @Override
    public SanPhamResponse update(SanPhamRequest sanPhamRequest, Long id) {
        SanPham sanPham = sanPhamRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SANPHAM_NOT_FOUND));

        validateForeignKeys(sanPhamRequest);

        updateEntityFromRequest(sanPham, sanPhamRequest);
        return SanPhamResponse.fromSanPham(sanPhamRepository.save(sanPham));
    }

    @Override
    public String delete(Long id) {
        if (sanPhamRepository.existsById(id)) {
            sanPhamRepository.deleteById(id);
            return "Deleted successfully";
        } else {
            throw new AppException(ErrorCode.SANPHAM_NOT_FOUND);
        }

    }

    @Override
    public List<SanPham> filterProducts(SanPhamFilterRequest filterRequest) {
        return List.of();
    }

    @Override
    public List<SanPhamCustumerResponse> listSanPhamGiamGia() {
        List<SanPham> sanPhams = sanPhamRepository.findAll();
        List<SanPham> sanPhamGiamGias = new ArrayList<>();
        for (SanPham sanPham : sanPhams) {
            if (sanPham.getTrangThai() == 1) {
                for (SanPhamChiTiet sanPhamChiTiet : sanPham.getSanPhamChiTietList()) {
                    SaleCt saleCts = sale_ctService.getSaleCtById(sanPhamChiTiet.getId());
                    if (saleCts != null) {
                        sanPhamGiamGias.add(sanPham);
                        break;
                    }
                }
            }
        }
        List<SanPhamCustumerResponse> sanPhamCustumerResponses = sanPhamGiamGias.stream().map(sanPham -> {
            // Lấy danh sách giá bán
            List<Double> giaBan = sanPham.getSanPhamChiTietList().stream()
                    .map(SanPhamChiTiet::getGiaBanSauKhiGiam)
                    .toList();

            // Tìm giá thấp nhất và cao nhất
            Double giaBanThapNhat = giaBan.stream().min(Double::compareTo).orElse(0.0);
            Double giaBanCaoNhat = giaBan.stream().max(Double::compareTo).orElse(0.0);

            // Hiển thị giá
            String giaHienThi = giaBanThapNhat.equals(giaBanCaoNhat)
                    ? String.format("%,.0f VND", giaBanThapNhat)
                    : String.format("%,.0f - %,.0f VND", giaBanThapNhat, giaBanCaoNhat);

            // Lấy hình ảnh đầu tiên
            String hinhAnh = sanPham.getSanPhamChiTietList().stream()
                    .flatMap(chiTiet -> chiTiet.getHinhAnhList().stream())
                    .map(HinhAnh::getUrl)
                    .findFirst()
                    .orElse(null);

            // Tìm giảm giá nếu có
            String phanTramGiamGia = "";
            for (SanPhamChiTiet sanPhamChiTiet : sanPham.getSanPhamChiTietList()) {
                SaleCt saleCts = sale_ctService.getSaleCtById(sanPhamChiTiet.getId());
                if (saleCts != null) {
//                    Double giaBanMoi = sanPhamChiTiet.getGiaBan() - saleCts.getTienGiam();
//                    giaBanThapNhat = Math.min(giaBanThapNhat, giaBanMoi);
                    phanTramGiamGia = saleCts.getGiaTriGiam().toString();
//                    giaHienThi = String.format("%,.0f VND", giaBanMoi);
                    break;
                }
            }

            // Tạo đối tượng SanPhamCustumerResponse
            return new SanPhamCustumerResponse(
                    sanPham.getId(),
                    sanPham.getTenSanPham(),
                    giaBanThapNhat,
                    giaBanCaoNhat,
                    giaHienThi,
                    hinhAnh,
                    phanTramGiamGia,
                    0l,
                    sanPham.getSanPhamChiTietList().size()
            );
        }).toList();
        return sanPhamCustumerResponses;
    }

    @Override
    public List<SanPhamCustumerResponse> listSanPhamBanChay() {
        // Lấy danh sách sản phẩm và tổng số lượng bán từ query
        List<Object[]> sanPhamData = sanPhamRepository.findBestSellingProductsNative();


        List<SanPhamCustumerResponse> sanPhamCustumerResponses = sanPhamData.stream().map(data -> {
            // Dữ liệu từ query
            SanPham sanPham = sanPhamRepository.findById((Long) data[0]).orElseThrow(() -> new AppException(ErrorCode.SANPHAM_NOT_FOUND));
            Long totalQuantity = ((BigDecimal) data[4]).longValue();
            ; // Số lượng bán (totalQuantity)

            // Lấy danh sách giá bán
            List<Double> giaBan = sanPham.getSanPhamChiTietList().stream()
                    .map(SanPhamChiTiet::getGiaBanSauKhiGiam)
                    .toList();

            // Tìm giá thấp nhất và cao nhất
            Double giaBanThapNhat = giaBan.stream().min(Double::compareTo).orElse(0.0);
            Double giaBanCaoNhat = giaBan.stream().max(Double::compareTo).orElse(0.0);

            // Hiển thị giá
            String giaHienThi = giaBanThapNhat.equals(giaBanCaoNhat)
                    ? String.format("%,.0f VND", giaBanThapNhat)
                    : String.format("%,.0f - %,.0f VND", giaBanThapNhat, giaBanCaoNhat);

            // Lấy hình ảnh đầu tiên
            String hinhAnh = sanPham.getSanPhamChiTietList().stream()
                    .flatMap(chiTiet -> chiTiet.getHinhAnhList().stream())
                    .map(HinhAnh::getUrl)
                    .findFirst()
                    .orElse(null);

            // Tìm giảm giá nếu có
            String phanTramGiamGia = "";
            for (SanPhamChiTiet sanPhamChiTiet : sanPham.getSanPhamChiTietList()) {
                SaleCt saleCts = sale_ctService.getSaleCtById(sanPhamChiTiet.getId());
                if (saleCts != null) {
//                    Double giaBanMoi = sanPhamChiTiet.getGiaBan() - saleCts.getTienGiam();
//                    giaBanThapNhat = Math.min(giaBanThapNhat, giaBanMoi);
                    phanTramGiamGia = saleCts.getGiaTriGiam().toString();
//                    giaHienThi = String.format("%,.0f VND", giaBanMoi);
                    break;
                }
            }

            // Tạo đối tượng SanPhamCustumerResponse
            return new SanPhamCustumerResponse(
                    sanPham.getId(),
                    sanPham.getTenSanPham(),
                    giaBanThapNhat,
                    giaBanCaoNhat,
                    giaHienThi,
                    hinhAnh,
                    phanTramGiamGia,
                    totalQuantity,
                    sanPham.getSanPhamChiTietList().size()
            );
        }).toList();

        return sanPhamCustumerResponses;
    }

    @Override
    public List<SanPhamResponse> listSanPhamMoi() {
        return sanPhamRepository.getSanPhamNewest().stream().map(SanPhamResponse::fromSanPham).toList();
    }


    private void validateForeignKeys(SanPhamRequest request) {
        if (!chatLieuDeRepository.existsById(request.getIdChatLieuDe())) {
            throw new AppException(ErrorCode.CHATLIEUDE_NOT_FOUND);
        }
        if (!danhMucRepository.existsById(request.getIdDanhMuc())) {
            throw new AppException(ErrorCode.DANHMUC_NOT_FOUND);
        }
        if (!thuongHieuRepository.existsById(request.getIdThuongHieu())) {
            throw new AppException(ErrorCode.THUONGHIEU_NOT_FOUND);
        }
        if (!chatLieuVaiRepository.existsById(request.getIdChatLieuVai())) {
            throw new AppException(ErrorCode.CHATLIEUVAI_NOT_FOUND);
        }
    }

    private void updateEntityFromRequest(SanPham sanPham, SanPhamRequest request) {
        sanPham.setTenSanPham(request.getTenSanPham())
                .setMoTa(request.getMoTa())
                .setTrangThai(request.getTrangThai())
                .setDanhMuc(danhMucRepository.findById(request.getIdDanhMuc())
                        .orElseThrow(() -> new AppException(ErrorCode.DANHMUC_NOT_FOUND)))
                .setChatLieuDe(chatLieuDeRepository.findById(request.getIdChatLieuDe())
                        .orElseThrow(() -> new AppException(ErrorCode.CHATLIEUDE_NOT_FOUND)))
                .setChatLieuVai(chatLieuVaiRepository.findById(request.getIdChatLieuVai())
                        .orElseThrow(() -> new AppException(ErrorCode.CHATLIEUVAI_NOT_FOUND)))
                .setThuongHieu(thuongHieuRepository.findById(request.getIdThuongHieu())
                        .orElseThrow(() -> new AppException(ErrorCode.THUONGHIEU_NOT_FOUND)));
    }

    private SanPham buildSanPham(SanPhamRequest request) {
        return SanPham.builder()
                .tenSanPham(request.getTenSanPham())
                .moTa(request.getMoTa())
                .chatLieuVai(chatLieuVaiRepository.findById(request.getIdChatLieuVai()).orElseThrow(() -> new AppException(ErrorCode.CHATLIEUVAI_NOT_FOUND)))
                .chatLieuDe(chatLieuDeRepository.findById(request.getIdChatLieuDe()).orElseThrow(() -> new AppException(ErrorCode.CHATLIEUDE_NOT_FOUND)))
                .thuongHieu(thuongHieuRepository.findById(request.getIdThuongHieu()).orElseThrow(() -> new AppException(ErrorCode.THUONGHIEU_NOT_FOUND)))
                .danhMuc(danhMucRepository.findById(request.getIdDanhMuc()).orElseThrow(() -> new AppException(ErrorCode.DANHMUC_NOT_FOUND)))
                .trangThai(request.getTrangThai())
                .build();
    }
}
