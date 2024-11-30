package com.example.be_datn.service.impl;


import com.example.be_datn.dto.Response.HoaDonChiTietResponse;
import com.example.be_datn.dto.Response.HoaDonResponse;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;
import java.util.List;

@Service
public class InvoicePdfGenerator {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font BOLD_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

    public ByteArrayInputStream generateInvoice(HoaDonResponse hoaDon, List<HoaDonChiTietResponse> chiTietList) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document, out);

            document.open();

            // Logo và Thông tin cửa hàng
            Paragraph storeName = new Paragraph("3HST Shoes", TITLE_FONT);
            storeName.setAlignment(Element.ALIGN_CENTER);
            document.add(storeName);

            Paragraph storeInfo = new Paragraph(
                    "Địa chỉ: 123 Đường ABC, Quận XYZ, Hà Nội\n" +
                            "Số điện thoại: 0123456789\n" +
                            "Email: contact@3hstshoes.com",
                    NORMAL_FONT
            );
            storeInfo.setAlignment(Element.ALIGN_CENTER);
            document.add(storeInfo);

            // Tiêu đề hóa đơn
            Paragraph invoiceTitle = new Paragraph("HÓA ĐƠN BÁN HÀNG", HEADER_FONT);
            invoiceTitle.setSpacingBefore(20);
            invoiceTitle.setSpacingAfter(10);
            invoiceTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(invoiceTitle);

            // Thông tin hóa đơn
            document.add(new Paragraph("Mã hóa đơn: " + hoaDon.getMaHoaDon(), NORMAL_FONT));
            document.add(new Paragraph("Ngày tạo: " + formatDateTime(hoaDon.getCreatedAt().toString()), NORMAL_FONT));
            document.add(new Paragraph("Khách hàng: " + hoaDon.getTenKhachHang(), NORMAL_FONT));
            document.add(new Paragraph("Số điện thoại: " + hoaDon.getSdt(), NORMAL_FONT));
            document.add(new Paragraph("Địa chỉ: " + hoaDon.getDiaChiNhan(), NORMAL_FONT));
            document.add(new Paragraph("Email: " + hoaDon.getEmail(), NORMAL_FONT));
            document.add(new Paragraph("Ghi chú: " + hoaDon.getGhiChu(), NORMAL_FONT));

            // Bảng chi tiết sản phẩm
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20);
            table.setSpacingAfter(20);

            // Header của bảng
            table.addCell(createCell("Tên sản phẩm", HEADER_FONT));
            table.addCell(createCell("Màu sắc", HEADER_FONT));
            table.addCell(createCell("Kích thước", HEADER_FONT));
            table.addCell(createCell("Số lượng", HEADER_FONT));
            table.addCell(createCell("Thành tiền", HEADER_FONT));

            // Nội dung bảng
            DecimalFormat formatter = new DecimalFormat("#,###");
            for (HoaDonChiTietResponse chiTiet : chiTietList) {
                table.addCell(createCell(chiTiet.getSanPhamChiTietResponse().getTenSanPham(), NORMAL_FONT));
                table.addCell(createCell(chiTiet.getSanPhamChiTietResponse().getTenMauSac(), NORMAL_FONT));
                table.addCell(createCell(chiTiet.getSanPhamChiTietResponse().getTenKichThuoc(), NORMAL_FONT));
                table.addCell(createCell(String.valueOf(chiTiet.getSoLuong()), NORMAL_FONT));
                table.addCell(createCell(formatter.format(chiTiet.getGiaTien() * chiTiet.getSoLuong()) + " đ", NORMAL_FONT));
            }

            document.add(table);

            // Thông tin tổng tiền
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(50);
            totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            totalTable.addCell(createCell("Tổng tiền hàng:", BOLD_FONT));
            totalTable.addCell(createCell(formatter.format(hoaDon.getTongTien() - hoaDon.getTienShip()) + " đ", NORMAL_FONT));

            totalTable.addCell(createCell("Phí vận chuyển:", BOLD_FONT));
            totalTable.addCell(createCell(formatter.format(hoaDon.getTienShip()) + " đ", NORMAL_FONT));

            if (hoaDon.getSoTienGiam() > 0) {
                totalTable.addCell(createCell("Giảm giá:", BOLD_FONT));
                totalTable.addCell(createCell("-" + formatter.format(hoaDon.getSoTienGiam()) + " đ", NORMAL_FONT));
            }

            totalTable.addCell(createCell("Tổng thanh toán:", BOLD_FONT));
            totalTable.addCell(createCell(formatter.format(hoaDon.getTienSauGiam()) + " đ", BOLD_FONT));

            document.add(totalTable);

            // Thông tin thanh toán
            Paragraph paymentInfo = new Paragraph(
                    "\nHình thức thanh toán: " + hoaDon.getHinhThucThanhToan().toUpperCase() +
                            "\nTrạng thái: " + hoaDon.getTrangThai(),
                    NORMAL_FONT
            );
            document.add(paymentInfo);

            // Chân trang
            Paragraph footer = new Paragraph(
                    "\n\nCảm ơn quý khách đã mua hàng tại 3HST Shoes!\n" +
                            "Mọi thắc mắc xin vui lòng liên hệ: 0123456789",
                    NORMAL_FONT
            );
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (DocumentException e) {
            throw new RuntimeException("Lỗi khi tạo PDF: " + e.getMessage());
        }
    }

    private PdfPCell createCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private String formatDateTime(String dateTime) {
        LocalDateTime dt = LocalDateTime.parse(dateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dt.format(formatter);
    }
}
