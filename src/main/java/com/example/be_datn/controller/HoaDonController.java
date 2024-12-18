package com.example.be_datn.controller;


import com.example.be_datn.dto.Request.HoaDonUpdateRequest;
import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Response.HoaDonResponse;
import com.example.be_datn.entity.HoaDon;
import com.example.be_datn.entity.KhachHang;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.HoaDonRepository;
import com.example.be_datn.repository.KhachHangRepository;
import com.example.be_datn.repository.LichSuHoaDonRepository;
import com.example.be_datn.service.impl.HoaDonChiTietService;
import com.example.be_datn.service.impl.HoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hoaDon")
public class HoaDonController {
    private final HoaDonService hoaDonService;
    private final HoaDonRepository hoaDonRepository;
    private final KhachHangRepository khachHangRepository;
    private final HoaDonChiTietService hoaDonChiTietService;
    private LichSuHoaDonRepository lichSuHoaDonRepository;


    @GetMapping
    public ApiResponse<?> getAllHoaDon(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        ApiResponse<Page<HoaDonResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(hoaDonRepository.findAllHoaDon(pageable));
        return apiResponse;
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getHoaDonById(@PathVariable Long id) {
        ApiResponse<HoaDonResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(hoaDonService.getHoaDonById(id));
        return apiResponse;
    }

    @PostMapping
    public ApiResponse<?> create() {
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setData(hoaDonService.createHoaDon());
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    private ApiResponse<?> delete(@PathVariable Long id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setData(hoaDonService.deleteHoaDon(id));
        return apiResponse;
    }

    @PutMapping("/{id}")
    public ApiResponse<?> update(
            @PathVariable Long id,
            @RequestBody HoaDonUpdateRequest request
    ) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setData(hoaDonService.updateHoaDon(id, request));
        return apiResponse;
    }

    @PatchMapping("/complete/{id}")
    public ApiResponse<?> completeOrder(@PathVariable Long id, @RequestParam String method, @RequestParam String diaChi
            , @RequestParam Double tienShip,
                                        @RequestParam String tenKhachHang,  @RequestParam String sdt, @RequestParam String ghiChu) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setData(hoaDonService.completeHoaDon(id, method, diaChi, tienShip, tenKhachHang, sdt, ghiChu));
        apiResponse.setCode(200);
        apiResponse.setMessage("Thanh toán thành công !");
        return apiResponse;
    }


    @PutMapping("/updateSoLuongAndTongTien/{id}")
    public ApiResponse<?> updateSoLuongAndTongTien(@PathVariable Long id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setData(hoaDonChiTietService.updateHoaDon(id));
        return apiResponse;
    }

    @PutMapping("/invoices/{idHoaDon}/addCustomer/{idKhachHang}")
    public ResponseEntity<String> addCustomerToInvoice(@PathVariable Long idHoaDon, @PathVariable Long idKhachHang) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new AppException(ErrorCode.HOA_DON_NOT_FOUND));

        KhachHang khachHang = khachHangRepository.findById(idKhachHang)
                .orElseThrow(() -> new AppException(ErrorCode.KHACH_HANG_NOT_FOUND));

        hoaDon.setKhachHang(khachHang);
        hoaDon.setTenNguoiNhan(khachHang.getTen());
        hoaDon.setSdt(khachHang.getSdt());
        hoaDon.setEmail(khachHang.getEmail());

        hoaDonRepository.save(hoaDon);

        return ResponseEntity.ok("Customer added to invoice successfully.");
    }

    @PutMapping("/changeTypeBill/{id}")
    public ApiResponse<?> changeTypeBill(@PathVariable Long id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        hoaDonService.changeTypeBill(id);
        return apiResponse;
    }


}
