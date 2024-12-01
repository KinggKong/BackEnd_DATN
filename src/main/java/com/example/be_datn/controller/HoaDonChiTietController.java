package com.example.be_datn.controller;


import com.example.be_datn.dto.Request.HoaDonChiTietRequest;
import com.example.be_datn.dto.Request.HoaDonChiTietUpdateRequest;
import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Response.HoaDonCTResponse;
import com.example.be_datn.dto.Response.HoaDonChiTietResponse;
import com.example.be_datn.entity.HoaDon;
import com.example.be_datn.entity.HoaDonCT;
import com.example.be_datn.repository.HoaDonChiTietRepository;
import com.example.be_datn.service.impl.HoaDonChiTietService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hdct")
public class HoaDonChiTietController {
    private final HoaDonChiTietService hoaDonChiTietService;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;

    @GetMapping("/{hoaDonId}")
    public ApiResponse<?> getAllHdctByHoaDonId(
            @PathVariable Long hoaDonId
    ){
        ApiResponse<List<HoaDonCTResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(hoaDonChiTietService.getAllHdctByIdHoaDon(hoaDonId));
        return apiResponse;
    }

    @PostMapping("/create")
    public ApiResponse<?> create(@RequestBody HoaDonChiTietRequest hoaDonChiTietRequest){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setData(hoaDonChiTietService.create(hoaDonChiTietRequest));
        return apiResponse;
    }
    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable Long id){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setData(hoaDonChiTietService.deleteHoaDonChiTiet(id));
        return apiResponse;
    }
    @PutMapping("/update-soLuong/{id}")
    public ApiResponse<?> updateSoLuong(@RequestBody HoaDonChiTietUpdateRequest request, @PathVariable Long id){
        ApiResponse<HoaDonCTResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(hoaDonChiTietService.update(request, id));
        return apiResponse;
    }

    @GetMapping("/detail/{id}")
    public ApiResponse<?> getDetail(@PathVariable Long id){
        ApiResponse<HoaDonCTResponse> apiResponse = new ApiResponse<>();
        HoaDonCT hoaDonCT = hoaDonChiTietRepository.findById(id).get();
        HoaDonCTResponse hoaDonCTResponse = new HoaDonCTResponse();
        hoaDonCTResponse.setId(id);
        hoaDonCTResponse.setIdSanPhamChiTiet(hoaDonCT.getSanPhamChiTiet().getId());
        hoaDonCTResponse.setIdGioHang(hoaDonCT.getHoaDon().getId());
        hoaDonCTResponse.setTenSanPhamChiTiet(hoaDonCT.getSanPhamChiTiet().getSanPham().getTenSanPham());
        hoaDonCTResponse.setSoLuong(hoaDonCT.getSoLuong());
        hoaDonCTResponse.setGiaBan(hoaDonCT.getSanPhamChiTiet().getGiaBan());
        hoaDonCTResponse.setTongTien((hoaDonCT.getSoLuong() * hoaDonCT.getSanPhamChiTiet().getGiaBan()));
        apiResponse.setData(hoaDonCTResponse);
        return apiResponse;
    }

}
