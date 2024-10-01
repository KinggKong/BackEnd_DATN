package com.example.be_datn.controller;

import com.example.be_datn.dto.ApiResponse;
import com.example.be_datn.dto.Request.ChatLieuVaiRequest;
import com.example.be_datn.dto.Response.ChatLieuVaiResponse;
import com.example.be_datn.service.IChatLieuVaiService;
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
@RequestMapping("/api/v1/chatlieuvais")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ChatLieuVaiController {
    IChatLieuVaiService chatLieuVaiService;

    @GetMapping("")
    ApiResponse<Page<ChatLieuVaiResponse>> getAllChatLieuVais(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                              @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                                              @RequestParam(name = "tenChatLieu", defaultValue = "") String tenChatLieu
    ) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize));
        ApiResponse<Page<ChatLieuVaiResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(chatLieuVaiService.getAllChatLieuVaiPageable(tenChatLieu, pageable));
        return apiResponse;
    }

    @PostMapping("")
    ApiResponse<ChatLieuVaiResponse> createChatLieuVai(@RequestBody @Valid ChatLieuVaiRequest chatLieuVaiRequest) {
        ApiResponse<ChatLieuVaiResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Thêm mới thành công chất liệu vải " + chatLieuVaiRequest.getTenChatLieuVai());
        apiResponse.setData(chatLieuVaiService.createChatLieuVai(chatLieuVaiRequest));
        return apiResponse;
    }

    @GetMapping("/{id}")
    ApiResponse<ChatLieuVaiResponse> getChatLieuVaiById(@PathVariable Long id) {
        ApiResponse<ChatLieuVaiResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(chatLieuVaiService.getChatLieuVaiById(id));
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteChatLieuVaiById(@PathVariable Long id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(chatLieuVaiService.deleteChatLieuVai(id));
        return apiResponse;
    }

    @PutMapping("/{id}")
    ApiResponse<ChatLieuVaiResponse> updateChatLieuVai(@PathVariable Long id, @RequestBody @Valid ChatLieuVaiRequest chatLieuVaiRequest) {
        ApiResponse<ChatLieuVaiResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Cập nhật thành công chất liệu vải");
        apiResponse.setData(chatLieuVaiService.updateChatLieuVai(id, chatLieuVaiRequest));
        return apiResponse;
    }
}
