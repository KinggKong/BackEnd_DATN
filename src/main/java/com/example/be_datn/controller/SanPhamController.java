package com.example.be_datn.controller;

import com.example.be_datn.dto.Request.SanPhamRequest;
import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Response.SanPhamCustumerResponse;
import com.example.be_datn.dto.Response.SanPhamResponse;
import com.example.be_datn.entity.HinhAnh;
import com.example.be_datn.entity.SaleCt;
import com.example.be_datn.entity.SanPham;
import com.example.be_datn.entity.SanPhamChiTiet;
import com.example.be_datn.service.impl.Sale_CTService;
import com.example.be_datn.service.impl.SanPhamService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/sanphams")
public class SanPhamController {
    SanPhamService sanPhamService;
    Sale_CTService sale_ctService;

//    @GetMapping("")
//    public ApiResponse<Page<SanPhamResponse>> getSanPhams(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
//                                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
//    ) {
//        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize));
//        ApiResponse<Page<SanPhamResponse>> apiResponse = new ApiResponse<>();
//        apiResponse.setData(sanPhamService.getAllPageable(pageable));
//        return apiResponse;
//    }

    @GetMapping("")
    public ApiResponse<Page<SanPhamResponse>> getSanPhamsWithFilter(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                                    @RequestParam(value = "idDanhMuc", defaultValue = "") Long idDahMuc,
                                                                    @RequestParam(value = "idThuongHieu", defaultValue = "") Long idThuongHieu,
                                                                    @RequestParam(value = "idChatLieuVai", defaultValue = "") Long idChatLieuVai,
                                                                    @RequestParam(value = "idChatLieuDe", defaultValue = "") Long idChatLieuDe,
                                                                    @RequestParam(value = "tenSanPham", defaultValue = "") String tenSanPham
    ) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize));
        ApiResponse<Page<SanPhamResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(sanPhamService.getAllWithFilter(idDahMuc, idThuongHieu,idChatLieuVai, idChatLieuDe, tenSanPham, pageable));
        return apiResponse;
    }
    @GetMapping("/get-all")
    public ApiResponse<List<SanPhamResponse>> getSanPhams(@RequestParam(value = "tenSanPham", defaultValue = "") String tenSanPham){
        ApiResponse<List<SanPhamResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(sanPhamService.getAllByTenSanPhamContaning(tenSanPham));
        return apiResponse;
    }



    @GetMapping("/{id}")
    public ApiResponse<SanPhamResponse> getSanPhamById(@PathVariable("id") Long id) {
        ApiResponse<SanPhamResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(sanPhamService.getById(id));
        return apiResponse;
    }

    @PostMapping("")
    public ApiResponse<SanPhamResponse> createSanPham(@RequestBody @Valid SanPhamRequest request) {
        ApiResponse<SanPhamResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(sanPhamService.create(request));
        apiResponse.setMessage("Thêm mới sản phẩm thành công");
        return apiResponse;
    }

    @PutMapping("/{id}")
    public ApiResponse<SanPhamResponse> updateSanPham(@PathVariable("id") Long id, @RequestBody SanPhamRequest sanPhamRequest) {
        ApiResponse<SanPhamResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(sanPhamService.update(sanPhamRequest, id));
        apiResponse.setMessage("Sửa sản phẩm thành công");
        return apiResponse;
    }

    @PutMapping("/status/{id}")
    public ApiResponse<SanPhamResponse> updateSanPhamStatus(@PathVariable("id") Long id,@RequestBody int trangThai) {
        ApiResponse<SanPhamResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Sửa sản phẩm thành công");
        apiResponse.setData(sanPhamService.updateStatus(id,trangThai));
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteSanPham(@PathVariable("id") Long id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(sanPhamService.delete(id));
        return apiResponse;
    }

    @GetMapping("/get-all-customer")
    public ApiResponse<Page<SanPhamCustumerResponse>> getSanPhamsCustumer(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize));
        ApiResponse<Page<SanPhamCustumerResponse>> apiResponse = new ApiResponse<>();
        Page<SanPham> sanPhams = sanPhamService.getAllPageableCustumer(pageable);
        Page<SanPhamCustumerResponse> sanPhamCustumerResponses = sanPhams.map(sanPham -> {
            List<Double> giaBan = sanPham.getSanPhamChiTietList().stream().map(SanPhamChiTiet::getGiaBan).toList();
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
                if(saleCts != null){
                    Double giaBanMoi = sanPhamChiTiet.getGiaBan() - saleCts.getTienGiam();
                    giaBanThapNhat = giaBanMoi;
                    phanTramGiamGia =saleCts.getGiaTriGiam().toString();
                    giaHienThi = String.format("%,.0f VND", giaBanMoi);
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
                    phanTramGiamGia
            );
        });
        apiResponse.setData(sanPhamCustumerResponses);
        apiResponse.setMessage("Lấy danh sách sản phẩm thành công");

        return apiResponse;
    }
    @GetMapping("/get-by-category/{id}")
    public ApiResponse<List<SanPhamCustumerResponse>> getSanPhamsCustumerByDanhMuc(@PathVariable("id") Integer id) {
        ApiResponse<List<SanPhamCustumerResponse>> apiResponse = new ApiResponse<>();

        // Lấy danh sách sản phẩm từ dịch vụ
        List<SanPham> sanPhams = sanPhamService.getSanPhamByDanhMucID(id);

        // Chuyển đổi danh sách sản phẩm thành danh sách phản hồi
        List<SanPhamCustumerResponse> sanPhamCustumerResponses = sanPhams.stream().map(sanPham -> {
            // Lấy danh sách giá bán của tất cả chi tiết sản phẩm
            List<Double> giaBan = sanPham.getSanPhamChiTietList().stream()
                    .map(SanPhamChiTiet::getGiaBan)
                    .collect(Collectors.toList());

            // Tìm giá thấp nhất và cao nhất
            Double giaBanThapNhat = giaBan.stream().min(Double::compareTo).orElse(0.0);
            Double giaBanCaoNhat = giaBan.stream().max(Double::compareTo).orElse(0.0);

            // Tạo chuỗi hiển thị giá
            String giaHienThi = giaBanThapNhat.equals(giaBanCaoNhat)
                    ? String.format("%,.0f VND", giaBanThapNhat)
                    : String.format("%,.0f - %,.0f VND", giaBanThapNhat, giaBanCaoNhat);

            // Lấy ảnh đầu tiên của sản phẩm
            String hinhAnh = sanPham.getSanPhamChiTietList().stream()
                    .flatMap(chiTiet -> chiTiet.getHinhAnhList().stream())
                    .map(HinhAnh::getUrl)
                    .findFirst()
                    .orElse(null);


            // Trả về đối tượng phản hồi
            return new SanPhamCustumerResponse(
                    sanPham.getId(),
                    sanPham.getTenSanPham(),
                    giaBanThapNhat,
                    giaBanCaoNhat,
                    giaHienThi,
                    hinhAnh,
                    ""
            );
        }).collect(Collectors.toList());  // Chuyển đổi Stream thành List

        // Đặt dữ liệu vào phản hồi
        apiResponse.setData(sanPhamCustumerResponses);
        apiResponse.setMessage("Lấy danh sách sản phẩm thành công");

        return apiResponse;
    }

}
