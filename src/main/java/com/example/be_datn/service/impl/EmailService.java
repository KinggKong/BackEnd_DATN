package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Response.HoaDonChiTietResponse;
import com.example.be_datn.dto.Response.InfoOrder;
import com.example.be_datn.dto.Response.SanPhamChiTietResponse;
import com.example.be_datn.service.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService implements IEmailService {
    JavaMailSender javaMailSender;

//    @Override
//    public String sendMailToUser(String to, String subject, String maHoaDon)  {
//        try {
//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//            InfoOrder infoOrder = shopOnlineService.getInfoOrder(maHoaDon);
//
//            helper.setTo(to);
//            helper.setSubject(subject);
//            String html = "";
//
//
//            helper.setText(html, true);
//
//            ClassPathResource imageResource = new ClassPathResource("/static/images/3HST.png");
//            helper.addInline("logoImage", imageResource);
//
//            javaMailSender.send(message);
//            return "success";
//        } catch (Exception e) {
//            return e.getMessage();
//        }
//    }

    @Override
    public String sendMailToUser(String to, String subject, String maHoaDon, InfoOrder infoOrder) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);

            String html = String.format("""
                            <!DOCTYPE html>
                            <html lang="vi">
                            <head>
                                <meta charset="UTF-8">
                                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                <title>Xác nhận đơn hàng - 3HST Shoes</title>
                            </head>
                            <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px;">
                            <div style="max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 3px rgba(0, 0, 0, 0.1);">
                                <div style="text-align: center; margin-bottom: 30px;">
                                    <img src="cid:logoImage" alt="3HST Shoes Logo" style="max-width: 150px; height: auto;">
                                </div>
                                <div style="text-align: center; background-color: #28a745; color: white; padding: 15px; border-radius: 5px; margin-bottom: 20px;">
                                    <h2 style="margin: 0;">Đặt hàng thành công!</h2>
                                </div>
                                <h3 style="color: #333333;">Xin chào %s,</h3>
                                <p style="color: #555555;">Cảm ơn bạn đã đặt hàng tại 3HST Shoes. Đơn hàng của bạn đã được xác nhận và đang được xử lý.</p>
                            
                                <div style="background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin: 20px 0;">
                                    <h3 style="color: #333333; margin-top: 0;">Thông tin đơn hàng:</h3>
                                    <p style="margin: 5px 0;"><strong>Mã đơn hàng:</strong> #%s</p>
                                    <p style="margin: 5px 0;"><strong>Ngày đặt:</strong> %s</p>
                                    <p style="margin: 5px 0;"><strong>Tổng giá trị:</strong> %s₫</p>
                                    <p style="margin: 5px 0;"><strong>Ghi chú:</strong> %s</p>
                                </div>
                            
                                <div style="text-align: center; margin: 30px 0;">
                                    <a href="https://3hst.com/invoice-lookup/%s" 
                                       style="background-color: #007bff; color: white; padding: 12px 25px; text-decoration: none; 
                                       border-radius: 5px; font-weight: bold; display: inline-block;">Theo dõi đơn hàng</a>
                                </div>
                            
                                <div style="margin-top: 30px; border-top: 1px solid #dee2e6; padding-top: 20px;">
                                    <h3 style="color: #333333;">Chi tiết đơn hàng:</h3>
                                    <table style="width: 100%%; border-collapse: collapse;">
                                        <tr style="background-color: #f8f9fa;">
                                            <th style="padding: 10px; text-align: left; border-bottom: 1px solid #dee2e6;">Sản phẩm</th>
                                            <th style="padding: 10px; text-align: right; border-bottom: 1px solid #dee2e6;">Số lượng</th>
                                            <th style="padding: 10px; text-align: right; border-bottom: 1px solid #dee2e6;">Giá</th>
                                        </tr>
                                        %s
                                        <tr style="background-color: #f8f9fa;">
                                            <td colspan="2" style="padding: 10px; text-align: right; font-weight: bold;">Tạm tính:</td>
                                            <td style="padding: 10px; text-align: right;">%s₫</td>
                                        </tr>
                                        <tr style="background-color: #f8f9fa;">
                                            <td colspan="2" style="padding: 10px; text-align: right; font-weight: bold;">Phí vận chuyển:</td>
                                            <td style="padding: 10px; text-align: right;">%s₫</td>
                                        </tr>
                                        <tr style="background-color: #f8f9fa;">
                                            <td colspan="2" style="padding: 10px; text-align: right; font-weight: bold;">Giảm giá:</td>
                                            <td style="padding: 10px; text-align: right;">%s₫</td>
                                        </tr>
                                        <tr style="background-color: #f8f9fa; font-size: 1.1em;">
                                            <td colspan="2" style="padding: 10px; text-align: right; font-weight: bold;">Tổng cộng:</td>
                                            <td style="padding: 10px; text-align: right; font-weight: bold; color: #28a745;">%s₫</td>
                                        </tr>
                                    </table>
                                </div>
                            
                                <div style="margin-top: 30px; background-color: #f8f9fa; padding: 15px; border-radius: 5px;">
                                    <h3 style="color: #333333; margin-top: 0;">Thông tin giao hàng:</h3>
                                    <p style="margin: 5px 0;"><strong>Người nhận:</strong> %s</p>
                                    <p style="margin: 5px 0;"><strong>Địa chỉ:</strong> %s</p>
                                    <p style="margin: 5px 0;"><strong>Số điện thoại:</strong> %s</p>
                                    <p style="margin: 5px 0;"><strong>Phương thức thanh toán:</strong> %s</p>
                                </div>
                            
                                <div style="margin-top: 30px; text-align: center; color: #6c757d;">
                                    <p style="margin: 5px 0;">Nếu bạn có bất kỳ thắc mắc nào, vui lòng liên hệ với chúng tôi:</p>
                                    <p style="margin: 5px 0;"><strong>Hotline:</strong> 1900 1234</p>
                                    <p style="margin: 5px 0;"><strong>Email:</strong> <a href="mailto:support@3hst.com" style="color: #007bff;">support@3hst.com</a></p>
                                </div>
                            
                                <div style="margin-top: 30px; padding-top: 20px; border-top: 1px solid #dee2e6;">
                                    <div style="text-align: center; color: #6c757d; font-size: 12px;">
                                        <p style="margin: 5px 0;">Email này được gửi tự động từ 3HST Shoes.</p>
                                        <p style="margin: 5px 0;">© 2024 3HST Shoes. All rights reserved.</p>
                                        <p style="margin: 5px 0;">Địa chỉ: 456 Đường DEF, Phường GHI, Quận 2, TP. Hồ Chí Minh</p>
                                    </div>
                                </div>
                            </div>
                            </body>
                            </html>
                            """,
                    infoOrder.getHoaDonResponse().getTenNguoiNhan(),
                    infoOrder.getHoaDonResponse().getMaHoaDon(),
                    formatDate(infoOrder.getHoaDonResponse().getCreatedAt().toString()),
                    formatCurrency(infoOrder.getHoaDonResponse().getTongTien()),
                    infoOrder.getHoaDonResponse().getGhiChu(),
                    infoOrder.getHoaDonResponse().getMaHoaDon(),
                    generateOrderItemsHtml(infoOrder.getHoaDonChiTietResponse()),
                    formatCurrency(infoOrder.getHoaDonResponse().getTongTien() - infoOrder.getHoaDonResponse().getTienShip() + infoOrder.getHoaDonResponse().getSoTienGiam()),
                    formatCurrency(infoOrder.getHoaDonResponse().getTienShip()),
                    formatCurrency(infoOrder.getHoaDonResponse().getSoTienGiam()),
                    formatCurrency(infoOrder.getHoaDonResponse().getTienSauGiam()),
                    infoOrder.getHoaDonResponse().getTenNguoiNhan(),
                    infoOrder.getHoaDonResponse().getDiaChiNhan(),
                    infoOrder.getHoaDonResponse().getSdt(),
                    getPaymentMethodText(infoOrder.getHoaDonResponse().getHinhThucThanhToan())
            );

            helper.setText(html, true);

            // Add logo image
            ClassPathResource imageResource = new ClassPathResource("/static/images/3HST.png");
            helper.addInline("logoImage", imageResource);

            // Add product images
            for (HoaDonChiTietResponse chiTiet : infoOrder.getHoaDonChiTietResponse()) {
                String imageUrl = chiTiet.getSanPhamChiTietResponse().getHinhAnhList().get(0).getUrl();
                String imageId = "product_" + chiTiet.getSanPhamChiTietResponse().getId();
                addImageFromUrl(helper, imageUrl, imageId);
            }

            javaMailSender.send(message);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String generateOrderItemsHtml(List<HoaDonChiTietResponse> items) {
        StringBuilder html = new StringBuilder();
        for (HoaDonChiTietResponse item : items) {
            SanPhamChiTietResponse sp = item.getSanPhamChiTietResponse();
            String imageUrl = sp.getHinhAnhList().get(0).getUrl();

            html.append(String.format("""
                            <tr>
                                <td style="padding: 10px; border-bottom: 1px solid #dee2e6;">
                                    <div style="display: flex; align-items: center;">
                                        <img src="cid:product_%d" alt="%s" style="margin-right: 10px; border-radius: 4px; width: 50px; height: 50px;">
                                        <div>
                                            <p style="margin: 0; font-weight: bold;">%s</p>
                                            <p style="margin: 0; color: #666; font-size: 0.9em;">Màu: %s | Size: %s</p>
                                        </div>
                                    </div>
                                </td>
                                <td style="padding: 10px; text-align: right; border-bottom: 1px solid #dee2e6;">%d</td>
                                <td style="padding: 10px; text-align: right; border-bottom: 1px solid #dee2e6;">%s₫</td>
                            </tr>
                            """,
                    sp.getId(),
                    sp.getTenSanPham(),
                    sp.getTenSanPham(),
                    sp.getTenMauSac(),
                    sp.getTenKichThuoc(),
                    item.getSoLuong(),
                    formatCurrency(item.getGiaTien())
            ));
        }
        return html.toString();
    }

    private void addImageFromUrl(MimeMessageHelper helper, String imageUrl, String contentId) throws Exception {
        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        ByteArrayDataSource dataSource = new ByteArrayDataSource(outputStream.toByteArray(), "image/jpeg");
        helper.addInline(contentId, dataSource);
    }

    private String formatDate(String dateStr) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime date = LocalDateTime.parse(dateStr, inputFormatter);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return date.format(outputFormatter);
        } catch (Exception e) {
            return dateStr;
        }
    }

    private String formatCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount);
    }

    private String getPaymentMethodText(String paymentMethod) {
        return switch (paymentMethod.toLowerCase()) {
            case "cod" -> "Thanh toán khi nhận hàng (COD)";
            case "bank_transfer" -> "Chuyển khoản ngân hàng";
            case "momo" -> "Ví điện tử MoMo";
            case "vnpay" -> "VNPay";
            default -> paymentMethod;
        };
    }


    @Override
    public String sendMailToAllUsers(String to, String subject, String body) {
        return "";
    }
}
