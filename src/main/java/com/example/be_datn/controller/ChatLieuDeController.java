package com.example.be_datn.controller;

import com.example.be_datn.DTO.Response.ApiResponse;
import com.example.be_datn.entity.ChatLieuDe;
import com.example.be_datn.service.IChatLieuDeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
    ApiResponse<List<ChatLieuDe>> getAllChatLieuDe(@RequestParam(defaultValue = "", name = "search") String name) {
        ApiResponse<List<ChatLieuDe>> apiResponse = new ApiResponse<>();
        apiResponse.setData(chatLieuDeService.getByName(name));
        return apiResponse;
    }
    @PostMapping("")
    ApiResponse<ChatLieuDe> createChatLieuDe(@RequestBody @Valid ChatLieuDe chatLieuDe) {
        chatLieuDe.setCreated_at(LocalDateTime.now());
        ApiResponse<ChatLieuDe> apiResponse = new ApiResponse<>();
        apiResponse.setData(chatLieuDeService.create(chatLieuDe));
        apiResponse.setMessage("Them thanh cong chat lieu de !");
        return apiResponse;
    }
    @GetMapping("/{id}")
    ApiResponse<ChatLieuDe> getChatLieuDeById(@PathVariable Long id) {
        ApiResponse<ChatLieuDe> apiResponse = new ApiResponse<>();
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
    ApiResponse<ChatLieuDe> updateChatLieuDe(@PathVariable Long id, @RequestBody @Valid ChatLieuDe chatLieuDe) {
        ApiResponse<ChatLieuDe> apiResponse = new ApiResponse<>();
        apiResponse.setData(chatLieuDeService.update(id, chatLieuDe));
        apiResponse.setMessage("Cập nhật thành công chất liệu");
        return apiResponse;
    }
}
