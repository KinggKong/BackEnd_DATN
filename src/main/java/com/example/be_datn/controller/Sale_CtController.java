package com.example.be_datn.controller;

import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Response.SaleCTResponse;
import com.example.be_datn.service.impl.Sale_CTService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/salect")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class Sale_CtController {
    Sale_CTService sale_ctService;
    @GetMapping("/get-by-spct/{id}")
    public ApiResponse<SaleCTResponse> getSaleCtByIdSanPhamCt(@PathVariable("id") Long id) {
        ApiResponse<SaleCTResponse> apiResponse = new ApiResponse<>();
        SaleCTResponse saleCTResponse = SaleCTResponse.fromSaleCT(sale_ctService.getSaleCtByIdSanPhamCt(id));

        apiResponse.setData(saleCTResponse);
        apiResponse.setMessage("Lấy thông tin chi tiết sale chi tiết thành công");
        return apiResponse;
    }
}
