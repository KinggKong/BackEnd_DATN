package com.example.be_datn.controller;

import com.example.be_datn.DTO.Response.ApiResponse;
import com.example.be_datn.model.request.FabricRequest;
import com.example.be_datn.model.response.FabricResponse;
import com.example.be_datn.service.FabricService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
@RequestMapping("/api/fabric")
@RequiredArgsConstructor
public class FabricController {
    private final FabricService fabricService;

    @GetMapping
    public ApiResponse<Page<FabricResponse>> gets(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                  @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        return ApiResponse.ok(fabricService.gets(page, size));
    }

    @PostMapping
    public ApiResponse<Object> create( @Valid @RequestBody FabricRequest request) {
        fabricService.create(request);
        return ApiResponse.ok(null);
    }

    @PutMapping("/{id}")
    public ApiResponse<Object> update(@Valid @RequestBody FabricRequest request,
                                       @PathVariable int id) {
        fabricService.update(request, id);
        return ApiResponse.ok(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> delete(@PathVariable int id) {
        fabricService.delete(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/{id}")
    public ApiResponse<FabricResponse> get(@PathVariable int id) {
        return ApiResponse.ok(fabricService.get(id));
    }
}
