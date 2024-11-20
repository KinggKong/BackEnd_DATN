package com.example.be_datn.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanPhamFilterRequest {

    private List<Long> danhMuc; // Ví dụ: ["Giày nam", "Giày xấu v~"]
    private List<Long> chatLieuDe; // Ví dụ: ["vàng", "cao su"]
    private List<Long> chatLieuVai; // Ví dụ: ["Da cá sấu", "Da hổ"]
    private Long thuongHieu; // Ví dụ: 1
    private BigDecimal minPrice; // Giá tối thiểu
    private BigDecimal maxPrice; // Giá tối đa
}
