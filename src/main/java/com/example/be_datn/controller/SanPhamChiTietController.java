package com.example.be_datn.controller;

import com.example.be_datn.dto.Request.SanPhamChiTietRequest;
import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Response.SanPhamChiTietResponse;
import com.example.be_datn.service.impl.SanPhamChiTietService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sanphamchitiets")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SanPhamChiTietController {
    SanPhamChiTietService sanPhamChiTietService;
    @GetMapping("")
    public ApiResponse<Page<SanPhamChiTietResponse>> getAllSanPhamChiTiet(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                                          @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                                                          @RequestParam(value = "idDanhMuc", defaultValue = "") Long idDahMuc,
                                                                          @RequestParam(value = "idThuongHieu", defaultValue = "") Long idThuongHieu,
                                                                          @RequestParam(value = "idChatLieuVai", defaultValue = "") Long idChatLieuVai,
                                                                          @RequestParam(value = "idChatLieuDe", defaultValue = "") Long idChatLieuDe,
                                                                          @RequestParam(value = "idSanPham", defaultValue = "") Long idSanPham) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize));
        ApiResponse<Page<SanPhamChiTietResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(sanPhamChiTietService.getAllByFilter(idDahMuc, idThuongHieu,idChatLieuVai, idChatLieuDe, idSanPham, pageable));
        return apiResponse;
    }
    @GetMapping("/get-all-by-sanpham/{id}")
    public ApiResponse<List<SanPhamChiTietResponse>> getSanPhamChiTietBySanPhamId(@PathVariable("id") Long id) {
        ApiResponse<List<SanPhamChiTietResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(sanPhamChiTietService.getAllBySanPhamId(id));
        return apiResponse;
    }
    @GetMapping("/exits")
    public ApiResponse<SanPhamChiTietResponse> getSPCTByMauSacAndKichThuoc(@RequestParam("idSp") Long idSp,
                                                                           @RequestParam("idMauSac") Long idMauSac,
                                                                           @RequestParam("idKichThuoc") Long idKichThuoc) {
        ApiResponse<SanPhamChiTietResponse> apiResponse = new ApiResponse<>();

        try {
            SanPhamChiTietResponse sanPhamChiTietResponse = sanPhamChiTietService.getSPCTByMauSacAndKichThuoc(idSp, idMauSac, idKichThuoc);
            apiResponse.setData(sanPhamChiTietResponse);
            apiResponse.setMessage("Lấy thông tin sản phẩm chi tiết thành công");
            apiResponse.setCode(HttpStatus.OK.value());
        } catch (EntityNotFoundException e) {
            apiResponse.setMessage("Không tìm thấy sản phẩm chi tiết với ID sản phẩm: " + idSp +
                    ", ID màu sắc: " + idMauSac +
                    ", ID kích thước: " + idKichThuoc);
            apiResponse.setCode(HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            apiResponse.setMessage("Đã xảy ra lỗi: " + e.getMessage());
            apiResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return apiResponse;
    }
    @GetMapping("/sanpham/{id}")
    public ApiResponse<Page<SanPhamChiTietResponse>> getAllSanPhamChiTietBySanPhamId(@PathVariable("id") Long id,
                                                                                    @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                                                    @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize));
        ApiResponse<Page<SanPhamChiTietResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(sanPhamChiTietService.getAllPageBySanPhamId(id, pageable));
        return apiResponse;
    }
    @PostMapping("")
    public ApiResponse<List<SanPhamChiTietResponse>> createSanPhamChiTiet(@RequestBody List<SanPhamChiTietRequest> sanPhamChiTietRequests) throws IOException {
        ApiResponse<List<SanPhamChiTietResponse>> apiResponse = new ApiResponse<>();

        apiResponse.setData(sanPhamChiTietService.create(sanPhamChiTietRequests));
        return apiResponse;
    }
    @PutMapping("/status/{id}")
    public ApiResponse<SanPhamChiTietResponse> updateStatus(@PathVariable("id") Long id, @RequestBody int status) {
        ApiResponse<SanPhamChiTietResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(sanPhamChiTietService.updateStatus(id, status));
        return apiResponse;
    }
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteSanPhamChiTiet(@PathVariable("id") Long id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(sanPhamChiTietService.delete(id));

        return apiResponse;
    }
    @PutMapping("/{id}")
    public ApiResponse<SanPhamChiTietResponse> updateSanPhamChiTiet(@PathVariable("id") Long id, @RequestBody SanPhamChiTietRequest sanPhamChiTietRequest) {
        ApiResponse<SanPhamChiTietResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(sanPhamChiTietService.update(id, sanPhamChiTietRequest));
        return apiResponse;
    }
    @GetMapping("/{id}")
    public ApiResponse<SanPhamChiTietResponse> getSanPhamChiTietById(@PathVariable("id") Long id) {
        ApiResponse<SanPhamChiTietResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(sanPhamChiTietService.getById(id));
        return apiResponse;
    }
}
