package com.example.be_datn.controller;

import com.example.be_datn.model.ResponseData;
import com.example.be_datn.model.request.ChatLieuVaiRequest;
import com.example.be_datn.model.response.ChatLuongVaiResponse;
import com.example.be_datn.service.ChatLieuVaiService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/chatlieu")
@RequiredArgsConstructor
@Validated
public class ChatLieuVaiController {
    private final ChatLieuVaiService chatLieuVaiService;
    private final HttpServletRequest servletRequest;

    @GetMapping
    public ResponseData<Page<ChatLuongVaiResponse>> gets(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                         @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        return ResponseData.ok(chatLieuVaiService.gets(page, size), servletRequest.getContextPath());
    }

    @PostMapping
    public ResponseData<Object> create( @Valid @RequestBody ChatLieuVaiRequest request) {
        chatLieuVaiService.create(request);
        return ResponseData.ok(null, servletRequest.getContextPath());
    }

    @PutMapping("/{id}")
    public ResponseData<Object> update(@Valid @RequestBody ChatLieuVaiRequest request,
                                       @PathVariable int id) {
        chatLieuVaiService.update(request, id);
        return ResponseData.ok(null, servletRequest.getContextPath());
    }

    @DeleteMapping("/{id}")
    public ResponseData<Object> delete(@PathVariable int id) {
        chatLieuVaiService.delete(id);
        return ResponseData.ok(null, servletRequest.getContextPath());
    }
}
