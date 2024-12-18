package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.KhachHangRequest1;
import com.example.be_datn.dto.Response.KhachHangResponse1;
import com.example.be_datn.entity.KhachHang;
import com.example.be_datn.entity.TaiKhoan;
import com.example.be_datn.entity.VaiTro;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.KhachHangRepository;
import com.example.be_datn.repository.TaiKhoanRepository;
import com.example.be_datn.repository.VaiTroRepository;
import com.example.be_datn.service.IKhachHangService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class KhachHangService implements IKhachHangService {

    JavaMailSender javaMailSender;
    KhachHangRepository khachHangRepository;
    TaiKhoanRepository taikhoanRepository;
    VaiTroRepository vaiTroRepository;
    GioHangService gioHangService;

    @Override
    public Page<KhachHangResponse1> getAllKhachHangPageable(String ten, Pageable pageable) {
        return khachHangRepository.findKhachHangByTenLike("%" + ten + "%", pageable)
                .map(KhachHangResponse1::fromKhachHang);
    }

    @Override
    public KhachHangResponse1 createKhachHang(KhachHangRequest1 khachHangRequest) throws MessagingException {
        // Kiểm tra xem có khách hàng với email hoặc số điện thoại đã tồn tại không
        if (khachHangRepository.existsKhachHangByEmail(khachHangRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (khachHangRepository.existsKhachHangBySdt(khachHangRequest.getSdt())) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);
        }

        // Tạo mã khách hàng tự động
        String maKhachHang = generateMaKhachHang(); // Gọi hàm sinh mã khách hàng

        // Tạo khách hàng mới từ dữ liệu request
        KhachHang khachHang = KhachHang.builder()
                .ten(khachHangRequest.getTen())
                .ma(maKhachHang)
                .email(khachHangRequest.getEmail())
                .sdt(khachHangRequest.getSdt())
                .avatar(khachHangRequest.getAvatar())
                .ngaySinh(khachHangRequest.getNgaySinh())
                .gioiTinh(khachHangRequest.getGioiTinh())
                .diaChi(khachHangRequest.getDiaChiStr())
                .trangThai(khachHangRequest.getTrangThai())
                .build();

        // Lưu khách hàng vào cơ sở dữ liệu
        KhachHang savedKhachHang = khachHangRepository.save(khachHang);

        // Tạo tài khoản cho khách hàng mới
        createAccount(savedKhachHang);

        // Tạo giỏ hàng cho khách hàng mới
        gioHangService.themGioHang(savedKhachHang.getId());

        return KhachHangResponse1.fromKhachHang(savedKhachHang);
    }

    private void createAccount(KhachHang khachHang) throws MessagingException {
        String random = generateAccountCode().substring(0, 5);
        String password = UUID.randomUUID().toString();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        VaiTro vaiTro = vaiTroRepository.findByTenVaiTro("ROLE_USER");
        if (vaiTro == null) {
            VaiTro newVaiTro = VaiTro.builder()
                    .tenVaiTro("ROLE_USER")
                    .trangThai(1)
                    .build();
            vaiTro = vaiTroRepository.save(newVaiTro);
        }

        // Sinh mã cho tài khoản, đảm bảo trường 'ma' không bị null
        String maTaiKhoan = "TK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        TaiKhoan taiKhoan = TaiKhoan.builder()
                .tenDangNhap(random + "_" + khachHang.getTen())
                .email(khachHang.getEmail())
                .matKhau(passwordEncoder.encode(password))
                .ownerID(khachHang.getId())
                .trangThai(1)
                .vaiTro(VaiTro.builder()
                        .id(vaiTro.getId())
                        .build())
                .ma(maTaiKhoan) // Gán mã cho tài khoản
                .build();
        taikhoanRepository.save(taiKhoan);

        // Gửi thông tin tài khoản cho khách hàng qua email
        sendInfoAccountForCustomer(khachHang.getEmail(), password);
    }

    // Phương thức sinh mã khách hàng tự động
    private String generateMaKhachHang() {
        return "KH" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateAccountCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }

    public void sendInfoAccountForCustomer(String email, String password) throws MessagingException {
        String subject = "Your New 3HST Shoes Shop Account";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String htmlContent = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>3HST Shoes Shop - New Customer Account</title>\n" +
                "  <style>\n" +
                "    body {\n" +
                "      font-family: 'Arial', sans-serif;\n" +
                "      background-color: #f4f7f6;\n" +
                "      color: #333;\n" +
                "      line-height: 1.6;\n" +
                "      margin: 0;\n" +
                "      padding: 0;\n" +
                "    }\n" +
                "    .container {\n" +
                "      max-width: 600px;\n" +
                "      margin: 20px auto;\n" +
                "      background-color: #ffffff;\n" +
                "      padding: 30px;\n" +
                "      border-radius: 8px;\n" +
                "      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);\n" +
                "    }\n" +
                "    .logo {\n" +
                "      text-align: center;\n" +
                "      margin-bottom: 20px;\n" +
                "      color: #007bff;\n" +
                "    }\n" +
                "    .credentials {\n" +
                "      background-color: #f8f9fa;\n" +
                "      border: 1px solid #e9ecef;\n" +
                "      border-radius: 5px;\n" +
                "      padding: 15px;\n" +
                "      margin: 20px 0;\n" +
                "    }\n" +
                "    .important-note {\n" +
                "      color: #dc3545;\n" +
                "      font-weight: bold;\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"container\">\n" +
                "    <div class=\"logo\">\n" +
                "      <h1>3HST Shoes Shop</h1>\n" +
                "    </div>\n" +
                "    <h2>Your New Account</h2>\n" +
                "    <p>Dear Customer,</p>\n" +
                "    <p>Welcome to 3HST Shoes Shop! Your customer account has been created. Please find your login credentials below:</p>\n" +
                "    <div class=\"credentials\">\n" +
                "      <p><strong>Email:</strong> " + email + "</p>\n" +
                "      <p><strong>Temporary Password:</strong> " + password + "</p>\n" +
                "    </div>\n" +
                "    <p class=\"important-note\">IMPORTANT: Please change your password upon first login.</p>\n" +
                "    <p>If you have any issues accessing your account, please contact our support team.</p>\n" +
                "    <p>Best regards,<br>3HST Shoes Shop</p>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>";

        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new AppException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    @Override
    public KhachHangResponse1 getKhachHangById(Long id) {
        return khachHangRepository.findById(id)
                .map(KhachHangResponse1::fromKhachHang)
                .orElseThrow(() -> new AppException(ErrorCode.KHACH_HANG_NOT_FOUND));
    }

    @Override
    public KhachHangResponse1 updateKhachHang(Long id, KhachHangRequest1 khachHangRequest) {
        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.KHACH_HANG_NOT_FOUND));

        khachHang.setTen(khachHangRequest.getTen());
        khachHang.setMa(khachHangRequest.getMa());
        khachHang.setEmail(khachHangRequest.getEmail());
        khachHang.setSdt(khachHangRequest.getSdt());
        khachHang.setAvatar(khachHangRequest.getAvatar());
        khachHang.setNgaySinh(khachHangRequest.getNgaySinh());
        khachHang.setDiaChi(khachHangRequest.getDiaChiStr());
        khachHang.setGioiTinh(khachHangRequest.getGioiTinh());
        khachHang.setTrangThai(khachHangRequest.getTrangThai());

        return KhachHangResponse1.fromKhachHang(khachHangRepository.save(khachHang));
    }

    @Override
    public String deleteKhachHang(Long id) {
        getKhachHangById(id);
        khachHangRepository.deleteById(id);
        return "deleted successfully";
    }
}