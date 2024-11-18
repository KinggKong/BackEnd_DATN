package com.example.be_datn.controller;


import com.example.be_datn.dto.Request.LichSuThanhToanCreationRequest;
import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.entity.LichSuThanhToan;
import com.example.be_datn.repository.LichSuThanhToanRepository;
import com.example.be_datn.service.impl.LichSuHoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lichSuThanhToan")
public class LichSuThanhToanController {

    private final LichSuHoaDonService lichSuHoaDonService;

    private final LichSuThanhToanRepository lichSuThanhToanRepository;

    @PostMapping
    public ApiResponse<?> create(@RequestBody LichSuThanhToanCreationRequest request){
        ApiResponse<LichSuThanhToan> res = new ApiResponse<>();
        res.setData(lichSuHoaDonService.create(request));
        res.setMessage("Tạo lịch sử thanh toán thành công !");
        return res;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable Long id){
        ApiResponse<LichSuThanhToan> res = new ApiResponse<>();
        lichSuHoaDonService.delete(id);
        return res;
    }

    @GetMapping("/getLichSuHoaDonByHoaDonId/{id}")
    public ApiResponse<?> getDetail(@PathVariable Long id){
        ApiResponse<LichSuThanhToan> res = new ApiResponse<>();
        lichSuThanhToanRepository.findByHoaDonIdNative(id);
        return res;
    }

}
