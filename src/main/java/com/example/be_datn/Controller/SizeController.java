package com.example.be_datn.Controller;

import com.example.be_datn.DTO.Response.ApiResponse;
import com.example.be_datn.Entity.Size;
import com.example.be_datn.Service.ISizeService;
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
@RequestMapping("/api/v1/size")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SizeController {
    ISizeService sizeService;

    @GetMapping("")
    ApiResponse<Page<Size>> getAllSize(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                       @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                       @RequestParam(name = "tenSize", defaultValue = "") String tenSize) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize));
        ApiResponse<Page<Size>> apiResponse = new ApiResponse<>();
        apiResponse.setData(sizeService.getAllSizePageable(tenSize, pageable));
        return apiResponse;
    }

    @PostMapping("")
    ApiResponse<Size> createSize(@RequestBody @Valid Size size) {
        ApiResponse<Size> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Thêm mới thành công kích thước " + size.getTenSize());
        apiResponse.setData(sizeService.createSize(size));
        return apiResponse;
    }

    @GetMapping("/{id}")
    ApiResponse<Size> getSizeById(@PathVariable Long id) {
        ApiResponse<Size> apiResponse = new ApiResponse<>();
        apiResponse.setData(sizeService.getSizeById(id));
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteSizeById(@PathVariable Long id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(sizeService.deleteSize(id));
        return apiResponse;
    }

    @PutMapping("/{id}")
    ApiResponse<Size> updateSize(@PathVariable Long id, @RequestBody @Valid Size size) {
        ApiResponse<Size> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Cập nhật thành công kích thước");
        apiResponse.setData(sizeService.updateSize(id, size));
        return apiResponse;
    }
}
