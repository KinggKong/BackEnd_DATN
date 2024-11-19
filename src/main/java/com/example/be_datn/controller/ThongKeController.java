package com.example.be_datn.controller;

import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Response.ThongKeResponse;

import com.example.be_datn.service.impl.ThongKeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/thongke")
public class ThongKeController {
    ThongKeService thongKeSerVice;
    @GetMapping("")
    public ApiResponse<ThongKeResponse> getThongKe(@RequestParam(value = "ngayBatDau", defaultValue = "") String ngayBatDau,
                                                   @RequestParam(value = "ngayKetThuc", defaultValue = "") String ngayKetThuc) {
        ApiResponse<ThongKeResponse> apiResponse = new ApiResponse<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault());
        LocalDateTime date1 = LocalDate.parse(ngayBatDau, formatter).atStartOfDay();
        LocalDateTime date2 = LocalDate.parse(ngayKetThuc, formatter).atTime(23, 59, 59);
//        Instant date1 = LocalDate.parse(ngayBatDau, formatter).atStartOfDay(ZoneId.systemDefault()).toInstant();
//        Instant date2 = LocalDate.parse(ngayKetThuc,formatter).atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
        //Instant date2 = LocalDate.parse(ngayKetThuc, formatter).atStartOfDay(ZoneId.systemDefault()).toInstant();
        apiResponse.setData(thongKeSerVice.getAll(date1,date2));
        return apiResponse;
    }

}


