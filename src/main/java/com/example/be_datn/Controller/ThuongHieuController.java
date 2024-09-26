package com.example.be_datn.Controller;

import com.example.be_datn.DTO.Response.ApiResponse;
import com.example.be_datn.Entity.ThuongHieu;
import com.example.be_datn.Service.IThuongHieuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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

@RestController
@RequestMapping("/api/v1/thuongHieu")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ThuongHieuController {
    IThuongHieuService thuongHieuService;

    @GetMapping("")
    ApiResponse<Page<ThuongHieu>> getAllThuongHieu(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                            @RequestParam(name = "tenThuongHieu",defaultValue = "") String tenThuongHieu
    ) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize));
        ApiResponse<Page<ThuongHieu>> apiResponse = new ApiResponse<>();
        apiResponse.setData(thuongHieuService.getAllThuongHieuPageable(tenThuongHieu,pageable));
        return apiResponse;
    }

    @PostMapping("")
    ApiResponse<ThuongHieu> createThuongHieu(@RequestBody @Valid ThuongHieu thuongHieu) {
        ApiResponse<ThuongHieu> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Thêm mới thành công màu " + thuongHieu.getTenThuongHieu());
        apiResponse.setData(thuongHieuService.createThuongHieu(thuongHieu));
        return apiResponse;
    }

    @GetMapping("/{id}")
    ApiResponse<ThuongHieu> getThuongHieuById(@PathVariable Long id) {
        ApiResponse<ThuongHieu> apiResponse = new ApiResponse<>();
        apiResponse.setData(thuongHieuService.getThuongHieuById(id));
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteThuongHieuById(@PathVariable Long id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(thuongHieuService.deleteThuongHieu(id));
        return apiResponse;
    }

    @PutMapping("/{id}")
    ApiResponse<ThuongHieu> updateThuongHieu(@PathVariable Long id, @RequestBody @Valid ThuongHieu thuongHieu) {
        ApiResponse<ThuongHieu> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Cập nhật thành công thuong hieu");
        apiResponse.setData(thuongHieuService.updateThuongHieu(id, thuongHieu));
        return apiResponse;
    }

}
