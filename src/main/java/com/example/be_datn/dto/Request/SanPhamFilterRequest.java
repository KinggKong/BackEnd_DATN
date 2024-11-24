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

    private List<Long> danhMucs; // Ví dụ: ["Giày nam", "Giày xấu v~"]
    private List<Long> chatLieuDes; // Ví dụ: ["vàng", "cao su"]
    private List<Long> chatLieuVais; // Ví dụ: ["Da cá sấu", "Da hổ"]
    private Long thuongHieu; // Ví dụ: 1
    private Double minPrice; // Giá tối thiểu
    private Double maxPrice; // Giá tối đa
    private String tenSanPham; // Tên sản phẩm
    private Integer page; // Trang hiện tại
    private Integer size; // Số lượng sản phẩm trên mỗi trang
}
