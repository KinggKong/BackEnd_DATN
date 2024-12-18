package com.example.be_datn.controller;

import com.example.be_datn.dto.Request.HoaDonRequest;
import com.example.be_datn.dto.Request.StatusBillRequest;
import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.service.ILichSuHoaDonService;
import com.example.be_datn.service.IShopOnlineService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shop-on")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShopOnlineController {
    IShopOnlineService shopOnlineService;
    ILichSuHoaDonService lichSuHoaDonService;

    @GetMapping("/confirm")
    public ApiResponse confirm(@RequestParam Long idKhachHang) {
        return ApiResponse.builder()
                .code(1000)
                .data(shopOnlineService.preCheckout(idKhachHang))
                .message("confirm precheckout successs")
                .build();
    }

    @PostMapping("/checkout")
    public ApiResponse checkout(@RequestBody HoaDonRequest hoaDonRequest) {
        return shopOnlineService.checkout(hoaDonRequest);
    }

    @GetMapping("/info-order")
    public ApiResponse<?> infoBill(@RequestParam String maHoaDon) {
        return ApiResponse.builder()
                .data(shopOnlineService.getInfoOrder(maHoaDon))
                .message("get info order successfull")
                .build();
    }

    @GetMapping("/order-management")
    public ApiResponse<?> orderManagement(@RequestParam String trangThai,
                                          @RequestParam(name = "keySearch", defaultValue = "") String keySearch,
                                          @RequestParam(value = "startDate", required = false) String startDate,
                                          @RequestParam(value = "endDate", required = false) String endDate
    ) {
        return ApiResponse.builder()
                .data(shopOnlineService.getAllOrderByStatus(trangThai, keySearch, startDate, endDate))
                .message("get order management successfull")
                .build();
    }

    @GetMapping("/detail-history-bill/{id}")
    public ApiResponse<?> detailHistoryBill(@PathVariable("id") Long idHoaDon) {
        return ApiResponse.builder()
                .data(shopOnlineService.getDetailHistoryBill(idHoaDon))
                .message("get detail history bill successfull")
                .build();
    }

    @PostMapping("/update-status-bill")
    public ApiResponse<?> updateStatusBill(@RequestBody StatusBillRequest statusBillRequest) {
        return ApiResponse.builder()
                .data(lichSuHoaDonService.insertLichSuHoaDon(statusBillRequest))
                .message("update status bill successfull")
                .build();
    }

    @GetMapping("/history-bill/{id}")
    public ApiResponse<?> historyBill(@PathVariable Long id,
                                      @RequestParam(name = "pageNumber", defaultValue = "1", required = true) int pageNumber,
                                      @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return ApiResponse.builder()
                .data(shopOnlineService.getAllHistoryBill(id, pageNumber, pageSize))
                .message("get history bill successfull")
                .build();
    }
}
