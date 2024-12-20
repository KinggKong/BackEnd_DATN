package com.example.be_datn.controller;

import com.example.be_datn.dto.Request.VoucherRequest;
import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Response.VoucherResponse;
import com.example.be_datn.service.impl.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/vouchers") // Đường dẫn cho voucher
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class VoucherController {
    VoucherService voucherService;

    @GetMapping("")
    ApiResponse<Page<VoucherResponse>> getAllVouchers(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                                      @RequestParam(value = "tenChienDich", defaultValue = "") String tenChienDich,
                                                      @RequestParam(value = "ngayBatDau", required = false) String ngayBatDauStr,
                                                      @RequestParam(value = "ngayKetThuc", required = false) String ngayKetThucStr,
                                                      @RequestParam(value = "trangThai", defaultValue = "") Integer trangThai // Thay đổi ở đây
    ) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize));
        ApiResponse<Page<VoucherResponse>> apiResponse = new ApiResponse<>();
        LocalDateTime ngayBatDau = null;
        LocalDateTime ngayKetThuc = null;


        if (ngayBatDauStr != null && !ngayBatDauStr.isEmpty()) {
            ngayBatDau = LocalDateTime.parse(ngayBatDauStr); // Chuyển đổi String thành LocalDateTime
        }
        if (ngayKetThucStr != null && !ngayKetThucStr.isEmpty()) {
            ngayKetThuc = LocalDateTime.parse(ngayKetThucStr);
        }
        apiResponse.setData(voucherService.getAllVoucherPageable(tenChienDich, ngayBatDau, ngayKetThuc, trangThai, pageable)); // Thay đổi ở đây
        return apiResponse;
    }


    @PostMapping("")
    ApiResponse<VoucherResponse> createVoucher(@RequestBody @Valid VoucherRequest voucherRequest) {
        ApiResponse<VoucherResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Thêm mới thành công voucher " + voucherRequest.getTenVoucher());
        apiResponse.setData(voucherService.createVoucher(voucherRequest));
        return apiResponse;
    }

    @GetMapping("/{id}")
    ApiResponse<VoucherResponse> getVoucherById(@PathVariable Long id) {
        ApiResponse<VoucherResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(voucherService.getVoucherById(id));
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteVoucherById(@PathVariable Long id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(voucherService.deleteVoucher(id));
        return apiResponse;
    }

    @PutMapping("/{id}")
    ApiResponse<VoucherResponse> updateVoucher(@PathVariable Long id, @RequestBody @Valid VoucherRequest voucherRequest) {
        ApiResponse<VoucherResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Cập nhật thành công voucher");
        apiResponse.setData(voucherService.updateVoucher(id, voucherRequest));
        return apiResponse;
    }

    @GetMapping("can-use")
    public ApiResponse getAllVoucherCanUser(@RequestParam Double tongTien) {
        return voucherService.getAllVoucherCanUser(tongTien);
    }
}
