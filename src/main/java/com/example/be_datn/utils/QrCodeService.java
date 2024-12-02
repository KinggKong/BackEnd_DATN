package com.example.be_datn.utils;

import com.example.be_datn.dto.Response.SanPhamChiTietResponse;

import java.io.IOException;

public interface QrCodeService {
    String generateQrCode(SanPhamChiTietResponse response);
    SanPhamChiTietResponse decodeQrCodeToSanPhamChiTiet(String qrCodeBase64) throws IOException;
}

