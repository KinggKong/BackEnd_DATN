package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Response.HoaDonChiTietResponse;
import com.example.be_datn.dto.Response.HoaDonResponse;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class InvoicePdfGenerator {

    private static Font TITLE_FONT;
    private static Font HEADER_FONT;
    private static Font NORMAL_FONT;
    private static Font BOLD_FONT;

    public InvoicePdfGenerator() {
        try {
            // Load Times New Roman font for better Vietnamese support
            BaseFont baseFont = BaseFont.createFont("times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            TITLE_FONT = new Font(baseFont, 18, Font.BOLD);
            HEADER_FONT = new Font(baseFont, 12, Font.BOLD);
            NORMAL_FONT = new Font(baseFont, 10, Font.NORMAL);
            BOLD_FONT = new Font(baseFont, 10, Font.BOLD);
        } catch (Exception e) {
            // Fallback to default fonts if Times New Roman is not available
            TITLE_FONT = new Font(Font.HELVETICA, 18, Font.BOLD);
            HEADER_FONT = new Font(Font.HELVETICA, 12, Font.BOLD);
            NORMAL_FONT = new Font(Font.HELVETICA, 10, Font.NORMAL);
            BOLD_FONT = new Font(Font.HELVETICA, 10, Font.BOLD);
        }
    }

    public ByteArrayInputStream generateInvoice(HoaDonResponse hoaDon, List<HoaDonChiTietResponse> chiTietList) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document, out);

            document.open();

            // Store information
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

            // Invoice title
            Paragraph invoiceTitle = new Paragraph("HÓA ĐƠN BÁN HÀNG", HEADER_FONT);
            invoiceTitle.setSpacingBefore(20);
            invoiceTitle.setSpacingAfter(10);
            invoiceTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(invoiceTitle);

            // Invoice information
            document.add(new Paragraph("Mã hóa đơn: " + hoaDon.getMaHoaDon(), NORMAL_FONT));
            document.add(new Paragraph("Ngày tạo: " + formatDateTime(hoaDon.getCreatedAt().toString()), NORMAL_FONT));
            document.add(new Paragraph("Khách hàng: " + hoaDon.getTenKhachHang(), NORMAL_FONT));
            document.add(new Paragraph("Số điện thoại: " + hoaDon.getSdt(), NORMAL_FONT));
            document.add(new Paragraph("Địa chỉ: " + hoaDon.getDiaChiNhan(), NORMAL_FONT));
            document.add(new Paragraph("Email: " + hoaDon.getEmail(), NORMAL_FONT));
            document.add(new Paragraph("Ghi chú: " + (hoaDon.getGhiChu() != null ? hoaDon.getGhiChu() : ""), NORMAL_FONT));

            // Product details table
            PdfPTable table = new PdfPTable(5);
            float[] columnWidths = {3f, 1f, 1f, 1f, 1.5f}; // Adjust column widths
            table.setWidths(columnWidths);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20);
            table.setSpacingAfter(20);

            // Table headers
            addTableHeader(table);

            // Table content
            DecimalFormat formatter = new DecimalFormat("#,###");
            for (HoaDonChiTietResponse chiTiet : chiTietList) {
                addTableRow(table, chiTiet, formatter);
            }

            document.add(table);

            // Total information
            PdfPTable totalTable = createTotalTable(hoaDon, formatter);
            document.add(totalTable);

            // Payment information
            Paragraph paymentInfo = new Paragraph(
                    "\nHình thức thanh toán: " + hoaDon.getHinhThucThanhToan() +
                            "\nTrạng thái: " + hoaDon.getTrangThai(),
                    NORMAL_FONT
            );
            document.add(paymentInfo);

            // Footer
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

    private void addTableHeader(PdfPTable table) {
        String[] headers = {"Tên sản phẩm", "Màu sắc", "Kích thước", "Số lượng", "Thành tiền"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(5);
            cell.setBackgroundColor(new Color(240, 240, 240));
            table.addCell(cell);
        }
    }

    private void addTableRow(PdfPTable table, HoaDonChiTietResponse chiTiet, DecimalFormat formatter) {
        table.addCell(createCell(chiTiet.getSanPhamChiTietResponse().getTenSanPham(), Element.ALIGN_LEFT));
        table.addCell(createCell(chiTiet.getSanPhamChiTietResponse().getTenMauSac(), Element.ALIGN_CENTER));
        table.addCell(createCell(chiTiet.getSanPhamChiTietResponse().getTenKichThuoc(), Element.ALIGN_CENTER));
        table.addCell(createCell(String.valueOf(chiTiet.getSoLuong()), Element.ALIGN_CENTER));
        table.addCell(createCell(formatter.format(chiTiet.getGiaTien() * chiTiet.getSoLuong()) + " đ", Element.ALIGN_RIGHT));
    }

    private PdfPTable createTotalTable(HoaDonResponse hoaDon, DecimalFormat formatter) {
        PdfPTable totalTable = new PdfPTable(2);
        totalTable.setWidthPercentage(50);
        totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalTable.setSpacingBefore(10);

        addTotalRow(totalTable, "Tổng tiền hàng:", formatter.format(hoaDon.getTongTien() - hoaDon.getTienShip()) + " đ");
        addTotalRow(totalTable, "Phí vận chuyển:", formatter.format(hoaDon.getTienShip()) + " đ");

        if (hoaDon.getSoTienGiam() > 0) {
            addTotalRow(totalTable, "Giảm giá:", "-" + formatter.format(hoaDon.getSoTienGiam()) + " đ");
        }

        // Add final total with bold font
        PdfPCell totalLabelCell = new PdfPCell(new Phrase("Tổng thanh toán:", BOLD_FONT));
        totalLabelCell.setBorder(Rectangle.TOP);
        totalLabelCell.setPadding(5);
        totalLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell totalValueCell = new PdfPCell(new Phrase(formatter.format(hoaDon.getTienSauGiam()) + " đ", BOLD_FONT));
        totalValueCell.setBorder(Rectangle.TOP);
        totalValueCell.setPadding(5);
        totalValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        totalTable.addCell(totalLabelCell);
        totalTable.addCell(totalValueCell);

        return totalTable;
    }

    private void addTotalRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, NORMAL_FONT));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(5);
        labelCell.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, NORMAL_FONT));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(5);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private PdfPCell createCell(String content, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content, NORMAL_FONT));
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        return cell;
    }

    private String formatDateTime(String dateTime) {
        LocalDateTime dt = LocalDateTime.parse(dateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dt.format(formatter);
    }
}

