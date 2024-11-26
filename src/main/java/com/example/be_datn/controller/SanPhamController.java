package com.example.be_datn.controller;

import com.example.be_datn.dto.Request.SanPhamFilterRequest;
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


    @GetMapping("/get-by-category/{id}")
    public ApiResponse<List<SanPhamCustumerResponse>> getSanPhamsCustumerByDanhMuc(@PathVariable("id") Integer id) {
        ApiResponse<List<SanPhamCustumerResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(sanPhamService.getSanPhamByDanhMucID(id));
        apiResponse.setMessage("Lấy danh sách sản phẩm thành công");
        return apiResponse;
    }
    @GetMapping("/get-all-customer-filter")
    public ApiResponse<Page<SanPhamCustumerResponse>> getSanPhamsCustumerFilter(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                                                @RequestParam(value = "danhMucs", required = false) List<Long> danhMucs,
                                                                                @RequestParam(value = "thuongHieu", required = false) List<Long> thuongHieus,
                                                                                @RequestParam(value = "chatLieuVais", required = false) List<Long> chatLieuVais,
                                                                                @RequestParam(value = "chatLieuDes", required = false) List<Long> chatLieuDes,
                                                                                @RequestParam(value = "tenSanPham", defaultValue = "") String tenSanPham,
                                                                                @RequestParam(value = "minPrice", defaultValue = "0") Double minPrice,
                                                                                @RequestParam(value = "maxPrice", defaultValue = "0") Double maxPrice,
                                                                                @RequestParam(value = "sortBy", defaultValue = "moiNhat") String sortBy){

        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize));
        // Xử lý tham số đầu vào


        ApiResponse<Page<SanPhamCustumerResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(sanPhamService.getAllPageableCustumerFilter(danhMucs,thuongHieus,chatLieuDes,chatLieuVais,tenSanPham,minPrice,maxPrice ,sortBy,pageable));
        return apiResponse;
    }
    @GetMapping("/get-all-customer-sale")
    public ApiResponse<List<SanPhamCustumerResponse>> getSanPhamsCustumerSale() {
        ApiResponse<List<SanPhamCustumerResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(sanPhamService.listSanPhamGiamGia());
        apiResponse.setMessage("Lấy danh sách sản phẩm thành công");
        return apiResponse;
    }
    @GetMapping("/get-all-customer-best-sell")
    public ApiResponse<List<SanPhamCustumerResponse>> getSanPhamsCustumerBestSell() {
        ApiResponse<List<SanPhamCustumerResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(sanPhamService.listSanPhamBanChay());
        apiResponse.setMessage("Lấy danh sách sản phẩm thành công");
        return apiResponse;
    }
}
