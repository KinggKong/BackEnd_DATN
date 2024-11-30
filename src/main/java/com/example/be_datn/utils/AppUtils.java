package com.example.be_datn.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class AppUtils {
//    public static String generateQrCode(String data, int width, int height) {
//        StringBuilder resultImage = new StringBuilder();
//
//        if (!data.isEmpty()) {
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//
//            try {
//                QRCodeWriter writer = new QRCodeWriter();
//                BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height);
//
//                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
//                ImageIO.write(bufferedImage, "png", os);
//
//                resultImage.append("data:image/png;base64,");
//                resultImage.append(new String(Base64.getEncoder().encode(os.toByteArray())));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return resultImage.toString();
//    }

    public static String generateQrCode(String data, int width, int height) {
        StringBuilder resultImage = new StringBuilder();

        if (data != null && !data.isEmpty()) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height);
                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
                ImageIO.write(bufferedImage, "png", os);

                // Chuyển đổi ảnh PNG thành chuỗi Base64
                resultImage.append("data:image/png;base64,");
                resultImage.append(Base64.getEncoder().encodeToString(os.toByteArray()));
            } catch (Exception e) {
                e.printStackTrace();
                resultImage.append("Error generating QR Code: " + e.getMessage());
            }
        } else {
            resultImage.append("Invalid data for QR Code generation.");
        }

        return resultImage.toString();
    }


    //    public static String prettyObject(Object object) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
public static String prettyObject(Object object) {
    try {
        ObjectMapper mapper = new ObjectMapper();
        // Đảm bảo rằng các kiểu LocalDateTime được xử lý đúng
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);  // Để không serialize date kiểu timestamp
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (JsonProcessingException e) {
        e.printStackTrace();
        return "Error converting object to JSON: " + e.getMessage();
    }
}

}
