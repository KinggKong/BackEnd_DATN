package com.example.be_datn.controller;

import com.example.be_datn.dto.Request.HoaDonRequest;
import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.dto.Response.HoaDonResponse;
import com.example.be_datn.service.impl.PaymentService;
import com.example.be_datn.service.impl.ShopOnlineService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class PaymentController {
    PaymentService paymentService;
    ShopOnlineService shopOnlineService;


    @PostMapping("/submitOrder")
    public ApiResponse submidOrder(@RequestBody HoaDonRequest hoaDonRequest,
                                   HttpServletRequest request) {

        HoaDonResponse hoaDonResponse = shopOnlineService.checkout(hoaDonRequest);
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        Integer amount = (int) Math.ceil(hoaDonResponse.getTongTien());
        String vnpayUrl = paymentService.createOrder(request, amount, hoaDonResponse.getMaHoaDon(), baseUrl);
        return ApiResponse.builder()
                .data(vnpayUrl)
                .message("Order submitted succesfully")
                .build();
    }

    @GetMapping("/vnpay-payment-return")
    public void paymentCompleted(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int paymentStatus = paymentService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        String redirectUrl = "http://localhost:5173/hanlde-result-payment?paymentStatus=" + paymentStatus
                + "&orderCode=" + orderInfo + "&paymentTime=" + paymentTime + "&transactionId=" + transactionId;

        response.sendRedirect(redirectUrl);
    }

}