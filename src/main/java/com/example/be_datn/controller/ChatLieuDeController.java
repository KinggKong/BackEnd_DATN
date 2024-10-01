package com.example.be_datn.controller;

import com.example.be_datn.dto.Request.ChatLieuDeRequest;
import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Response.ChatLieuDeResponse;
import com.example.be_datn.entity.ChatLieuDe;
import com.example.be_datn.service.IChatLieuDeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chatlieudes")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ChatLieuDeController {
    IChatLieuDeService chatLieuDeService;

    @GetMapping("")
    ApiResponse<Page<ChatLieuDeResponse>> getAllChatLieuDe(
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "", name = "tenChatLieuDe") String name) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize));
        ApiResponse<Page<ChatLieuDeResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(chatLieuDeService.getByName(name,pageable));
        return apiResponse;
    }
    @PostMapping("")
    ApiResponse<ChatLieuDeResponse> createChatLieuDe(@RequestBody @Valid ChatLieuDeRequest chatLieuDe) {

        ApiResponse<ChatLieuDeResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(chatLieuDeService.create(chatLieuDe));
        apiResponse.setMessage("Them thanh cong chat lieu de !");
        return apiResponse;
    }
    @GetMapping("/{id}")
    ApiResponse<ChatLieuDeResponse> getChatLieuDeById(@PathVariable Long id) {
        ApiResponse<ChatLieuDeResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(chatLieuDeService.getById(id));

        return apiResponse;
    }
    @DeleteMapping("/{id}")
    ApiResponse<String> deleteChatLieuDeById(@PathVariable Long id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(chatLieuDeService.delete(id));

        return apiResponse;
    }
    @PutMapping("/{id}")
    ApiResponse<ChatLieuDeResponse> updateChatLieuDe(@PathVariable Long id, @RequestBody @Valid ChatLieuDeRequest chatLieuDe) {
        ApiResponse<ChatLieuDeResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(chatLieuDeService.update(id, chatLieuDe));
        apiResponse.setMessage("Cập nhật thành công chất liệu");
        return apiResponse;
    }
}
