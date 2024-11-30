    package com.example.be_datn.utils;

    import com.example.be_datn.dto.Response.SanPhamChiTietResponse;
    import com.example.be_datn.entity.SanPhamChiTiet;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.google.zxing.BinaryBitmap;
    import com.google.zxing.Reader;
    import com.google.zxing.Result;
    import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
    import com.google.zxing.common.HybridBinarizer;
    import com.google.zxing.qrcode.QRCodeReader;
    import org.springframework.stereotype.Service;

    import javax.imageio.ImageIO;
    import java.awt.image.BufferedImage;
    import java.io.ByteArrayInputStream;
    import java.io.IOException;
    import java.util.Base64;

    @Service
    public class QrCodeServiceImpl implements QrCodeService{
        private static final int ORDER_QR_CODE_SIZE_WIDTH = 300;
        private static final int ORDER_QR_CODE_SIZE_HEIGHT = 300;

        @Override
        public String generateQrCode(SanPhamChiTietResponse response) {
            String prettyData = AppUtils.prettyObject(response);
            String qrCode = AppUtils.generateQrCode(prettyData, ORDER_QR_CODE_SIZE_WIDTH, ORDER_QR_CODE_SIZE_HEIGHT);
            return qrCode;
        }

        @Override
        public SanPhamChiTietResponse decodeQrCodeToSanPhamChiTiet(String qrCodeBase64) throws IOException {
            byte[] decodedBytes = Base64.getDecoder().decode(qrCodeBase64.split(",")[1]);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodedBytes);
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);

            // Sử dụng ZXing để quét QR và lấy dữ liệu từ mã QR
            try {
                BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
                Reader reader = new QRCodeReader();
                Result result = reader.decode(binaryBitmap);
                String qrContent = result.getText();  // Dữ liệu JSON

                // Chuyển đổi chuỗi JSON thành đối tượng SanPhamChiTietResponse
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(qrContent, SanPhamChiTietResponse.class);

            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException("Failed to decode QR Code", e);
            }
        }
    }
