//    package com.example.be_datn.utils;
//
//    import com.example.be_datn.dto.Response.SanPhamChiTietResponse;
//    import com.example.be_datn.entity.SanPhamChiTiet;
//    import com.fasterxml.jackson.databind.ObjectMapper;
//    import com.google.zxing.BinaryBitmap;
//    import com.google.zxing.Reader;
//    import com.google.zxing.Result;
//    import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
//    import com.google.zxing.common.HybridBinarizer;
//    import com.google.zxing.qrcode.QRCodeReader;
//    import org.springframework.stereotype.Service;
//
//    import javax.imageio.ImageIO;
//    import java.awt.image.BufferedImage;
//    import java.io.ByteArrayInputStream;
//    import java.io.IOException;
//    import java.util.Base64;
//
//    @Service
//    public class QrCodeServiceImpl implements QrCodeService{
//        private static final int ORDER_QR_CODE_SIZE_WIDTH = 300;
//        private static final int ORDER_QR_CODE_SIZE_HEIGHT = 300;
//
//        @Override
//        public String generateQrCode(SanPhamChiTietResponse response) {
//            String prettyData = AppUtils.prettyObject(response);
//            String qrCode = AppUtils.generateQrCode(prettyData, ORDER_QR_CODE_SIZE_WIDTH, ORDER_QR_CODE_SIZE_HEIGHT);
//            return qrCode;
//        }
//
//        @Override
//        public SanPhamChiTietResponse decodeQrCodeToSanPhamChiTiet(String qrCodeBase64) throws IOException {
//            byte[] decodedBytes = Base64.getDecoder().decode(qrCodeBase64.split(",")[1]);
//            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodedBytes);
//            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
//
//            // Sử dụng ZXing để quét QR và lấy dữ liệu từ mã QR
//            try {
//                BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
//                Reader reader = new QRCodeReader();
//                Result result = reader.decode(binaryBitmap);
//                String qrContent = result.getText();  // Dữ liệu JSON
//
//                // Chuyển đổi chuỗi JSON thành đối tượng SanPhamChiTietResponse
//                ObjectMapper objectMapper = new ObjectMapper();
//                return objectMapper.readValue(qrContent, SanPhamChiTietResponse.class);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new IOException("Failed to decode QR Code", e);
//            }
//        }
//    }

package com.example.be_datn.utils;

import com.example.be_datn.dto.Response.SanPhamChiTietResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.*;
import com.google.zxing.Reader;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class QrCodeServiceImpl implements QrCodeService {
    private static final int ORDER_QR_CODE_SIZE_WIDTH = 300;
    private static final int ORDER_QR_CODE_SIZE_HEIGHT = 300;
    private static final String QR_CODE_DIRECTORY = "qrcodes/";

    @Override
    public String generateQrCode(SanPhamChiTietResponse response) throws IOException {
        try {
            String prettyData = AppUtils.prettyObject(response);
            String fileName = QR_CODE_DIRECTORY + "qr_" + response.getId() + ".png";

            // Tạo thư mục nếu chưa tồn tại
            Path directoryPath = Paths.get(QR_CODE_DIRECTORY);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            // Tạo QR Code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(prettyData, BarcodeFormat.QR_CODE, ORDER_QR_CODE_SIZE_WIDTH, ORDER_QR_CODE_SIZE_HEIGHT);

            // Lưu file QR Code
            Path filePath = Paths.get(fileName);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);

            return fileName; // Trả về đường dẫn file
        } catch (WriterException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to encode QR Code", e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to write QR Code to file", e);
        }
    }


    @Override
    public SanPhamChiTietResponse decodeQrCodeToSanPhamChiTiet(String qrCodeBase64OrFilePath) throws IOException {
        BufferedImage qrImage;

        if (qrCodeBase64OrFilePath.startsWith("data:image")) {
            // Decode từ base64
            byte[] decodedBytes = Base64.getDecoder().decode(qrCodeBase64OrFilePath.split(",")[1]);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodedBytes);
            qrImage = ImageIO.read(byteArrayInputStream);
        } else {
            // Decode từ file
            File file = new File(qrCodeBase64OrFilePath);
            qrImage = ImageIO.read(file);
        }

        // Sử dụng ZXing để quét QR và lấy dữ liệu từ mã QR
        try {
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(qrImage)));
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

