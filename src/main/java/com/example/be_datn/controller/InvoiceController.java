package com.example.be_datn.controller;

import com.example.be_datn.dto.Response.HoaDonResponse;
import com.example.be_datn.dto.Response.InfoOrder;
import com.example.be_datn.service.impl.InvoicePdfGenerator;
import com.example.be_datn.service.impl.ShopOnlineService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/invoice")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceController {
    ShopOnlineService service;
    private InvoicePdfGenerator pdfGenerator;

    @GetMapping("/generate")
    public ResponseEntity<InputStreamResource> generateInvoice(@RequestParam String maHoaDon) {
        InfoOrder infoOrder = service.getInfoOrder(maHoaDon);

        ByteArrayInputStream bis = pdfGenerator.generateInvoice(infoOrder.getHoaDonResponse(), infoOrder.getHoaDonChiTietResponse());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=invoice-" + infoOrder.getHoaDonResponse().getMaHoaDon() + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
