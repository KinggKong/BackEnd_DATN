package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.NhanVienRequest;
import com.example.be_datn.dto.Response.NhanVienResponse;
import com.example.be_datn.entity.NhanVien;
import com.example.be_datn.entity.TaiKhoan;
import com.example.be_datn.entity.VaiTro;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.GioHangRepository;
import com.example.be_datn.repository.NhanVienRepository;
import com.example.be_datn.repository.TaiKhoanRepository;
import com.example.be_datn.repository.VaiTroRepository;
import com.example.be_datn.service.INhanVienService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class NhanVienService implements INhanVienService {
    JavaMailSender javaMailSender;
    NhanVienRepository nhanVienRepository;
    TaiKhoanService taikhoanService;
    TaiKhoanRepository taikhoanRepository;
    GioHangRepository gioHangRepository;
    private final VaiTroRepository vaiTroRepository;


    @Override
    public Page<NhanVienResponse> getAllNhanVienPageable(String ten, Pageable pageable) {
        // Lọc nhân viên theo tên và phân trang
        return nhanVienRepository.findNhanVienByTenLike("%" + ten + "%", pageable)
                .map(NhanVienResponse::fromNhanVien);
    }

    @Override
    public NhanVienResponse createNhanVien(NhanVienRequest nhanVienRequest) throws MessagingException {
        // Kiểm tra xem có nhân viên với email hoặc số điện thoại đã tồn tại không
        if (nhanVienRepository.existsNhanVienByEmail(nhanVienRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (taikhoanRepository.existsByEmail(nhanVienRequest.getEmail())) {
            throw new AppException(ErrorCode.ACCOUNT_EMAIL_EXISTED);
        }
        if (nhanVienRepository.existsNhanVienBySdt(nhanVienRequest.getSdt())) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);
        }

        // Tạo nhân viên mới từ dữ liệu request
        NhanVien nhanVien = NhanVien.builder()
                .ten(nhanVienRequest.getTen())
                .email(nhanVienRequest.getEmail())
                .sdt(nhanVienRequest.getSdt())
                .avatar(nhanVienRequest.getAvatar())
                .ngaySinh(nhanVienRequest.getNgaySinh())
                .diaChi(nhanVienRequest.getDiaChi())
                .gioiTinh(nhanVienRequest.getGioiTinh())
                .trangThai(nhanVienRequest.getTrangThai())
                .build();

        // Lưu nhân viên vào cơ sở dữ liệu
        NhanVien savedNhanVien = nhanVienRepository.saveAndFlush(nhanVien);
        createAccount(savedNhanVien);
        return NhanVienResponse.fromNhanVien(savedNhanVien);
    }

    @Override
    public NhanVienResponse getNhanVienById(Long id) {
        // Tìm nhân viên theo ID
        return nhanVienRepository.findById(id)
                .map(NhanVienResponse::fromNhanVien)
                .orElseThrow(() -> new AppException(ErrorCode.NHANVIEN_NOT_FOUND));
    }

    @Override
    public NhanVienResponse updateNhanVien(Long id, NhanVienRequest nhanVienRequest) {
        // Tìm nhân viên theo ID
        NhanVien nhanVien = nhanVienRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NHANVIEN_NOT_FOUND));

        // Cập nhật thông tin nhân viên
        nhanVien.setTen(nhanVienRequest.getTen());
        nhanVien.setEmail(nhanVienRequest.getEmail());
        nhanVien.setSdt(nhanVienRequest.getSdt());
        nhanVien.setAvatar(nhanVienRequest.getAvatar());
        nhanVien.setNgaySinh(nhanVienRequest.getNgaySinh());
        nhanVien.setDiaChi(nhanVienRequest.getDiaChi());
        nhanVien.setGioiTinh(nhanVienRequest.getGioiTinh());
        nhanVien.setTrangThai(nhanVienRequest.getTrangThai());

        // Lưu cập nhật vào cơ sở dữ liệu
        return NhanVienResponse.fromNhanVien(nhanVienRepository.save(nhanVien));
    }

    @Override
    public String deleteNhanVien(Long id) {
        // Kiểm tra sự tồn tại của nhân viên trước khi xóa
        getNhanVienById(id);
        nhanVienRepository.deleteById(id);
        return "deleted successfully";
    }

    private void createAccount(NhanVien nhanVien) throws MessagingException {
        String random = generateAccountCode().substring(0, 5);
        String password = UUID.randomUUID().toString();
        String ma = generateAccountCode();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        VaiTro vaiTro = vaiTroRepository.findByTenVaiTro("ROLE_STAFF");
        if (vaiTro == null) {
            VaiTro newVaiTro = VaiTro.builder()
                    .tenVaiTro("ROLE_STAFF")
                    .trangThai(1)
                    .build();
            vaiTro = vaiTroRepository.saveAndFlush(newVaiTro);
        }
//        String password = generateAccountCode();
        TaiKhoan taiKhoan = TaiKhoan.builder()
                .tenDangNhap(random + "_" + nhanVien.getTen())
                .email(nhanVien.getEmail())
                .ma(ma)
                .matKhau(passwordEncoder.encode(password))
                .ownerID(nhanVien.getId())
                .trangThai(1)
                .vaiTro(VaiTro.builder()
                        .id(vaiTro.getId())
                        .build())
                .build();
        TaiKhoan result = taikhoanRepository.save(taiKhoan);
        if (taiKhoan != null) {
            sendInfoAccountForEmployee(nhanVien.getEmail(), password);
        }
    }

    public static String generateAccountCode() {
        String uuid = UUID.randomUUID().toString();
        String accountCode = uuid.replace("-", "").substring(0, 10).toUpperCase();
        return accountCode;
    }


    @Async
    public String sendInfoAccountForEmployee(String email, String password) throws MessagingException {
        if (!isValidEmail(email.trim())) {
            throw new AppException(ErrorCode.EMAIL_INCORRECT_FORMAT);
        }

        String subtitle = "Your New 3HST Shoes Shop Employee Account";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>3HST Shoes Shop - New Employee Account</title>\n" +
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
                "    <h2>Your New Employee Account</h2>\n" +
                "    <p>Dear New Team Member,</p>\n" +
                "    <p>Welcome to 3HST Shoes Shop! Your employee account has been created. Please find your login credentials below:</p>\n" +
                "    <div class=\"credentials\">\n" +
                "      <p><strong>Email:</strong> {{employeeEmail}}</p>\n" +
                "      <p><strong>Temporary Password:</strong> {{temporaryPassword}}</p>\n" +
                "    </div>\n" +
                "    <p class=\"important-note\">IMPORTANT: Please change your password upon first login.</p>\n" +
                "    <p>If you have any issues accessing your account, please contact the IT support team.</p>\n" +
                "    <p>Best regards,<br>3HST Shoes Shop Management</p>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>";

        html = html.replace("{{employeeEmail}}", email)
                .replace("{{temporaryPassword}}", password);


        helper.setTo(email);
        helper.setSubject(subtitle);
        helper.setText(html, true);
        try {
            javaMailSender.send(message);
            return "Employee account credentials sent successfully";
        } catch (MailException e) {
            throw new AppException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
