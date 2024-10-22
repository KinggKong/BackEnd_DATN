package com.example.be_datn.controller;

import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.entity.HinhAnh;
import com.example.be_datn.service.impl.HinhAnhService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hinhanhs")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class HinhAnhController {
    HinhAnhService hinhAnhService;
    @GetMapping("")
    public ApiResponse<List<HinhAnh>> getAllHinhAnh() {
        ApiResponse<List<HinhAnh>> apiResponse = new ApiResponse<>();
        apiResponse.setData(hinhAnhService.getAll());
        return apiResponse;
    }
    @GetMapping("/url/{id}")
    public ApiResponse<List<String>> getHinhAnhByUrl(@PathVariable Long id) {
        ApiResponse<List<String>> apiResponse = new ApiResponse<>();
        apiResponse.setData( hinhAnhService.getAllByIdSanPhamCt(id).stream().map(HinhAnh::getUrl).toList());
        return apiResponse;
    }
}
