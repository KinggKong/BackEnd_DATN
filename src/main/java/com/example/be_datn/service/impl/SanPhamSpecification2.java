package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Request.SanPhamFilterRequest;
import com.example.be_datn.entity.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SanPhamSpecification2 implements Specification<SanPham> {
        private final SanPhamFilterRequest filterRequest;

    public SanPhamSpecification2(SanPhamFilterRequest filterRequest) {
        this.filterRequest = filterRequest;
    }

    @Override
    public Predicate toPredicate(Root<SanPham> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        // Join với ProductDetail
        Join<SanPham, SanPhamChiTiet> productDetailJoin = root.join("sanPhamChiTietList", JoinType.INNER);
        Join<SanPham, DanhMuc> danhMucJoin = root.join("danhMuc", JoinType.INNER);
        Join<SanPham, ChatLieuDe> chatLieuDeJoin = root.join("chatLieuDe", JoinType.INNER);
        Join<SanPham, ChatLieuVai> chatLieuVaiJoin = root.join("chatLieuVai", JoinType.INNER);
        Join<SanPham,ThuongHieu> thuongHieuJoin = root.join("thuongHieu", JoinType.INNER);
        // Lọc theo danh mục (danhMuc)
        if (filterRequest.getDanhMuc() != null && !filterRequest.getDanhMuc().isEmpty()) {
            predicates.add(danhMucJoin.get("id").in(filterRequest.getDanhMuc()));
        }

        // Lọc theo chất liệu đế (chatLieuDe)
        if (filterRequest.getChatLieuDe() != null && !filterRequest.getChatLieuDe().isEmpty()) {
            predicates.add(chatLieuDeJoin.get("id").in(filterRequest.getChatLieuDe()));
        }

        // Lọc theo chất liệu vải (chatLieuVai)
        if (filterRequest.getChatLieuVai() != null && !filterRequest.getChatLieuVai().isEmpty()) {
            predicates.add(chatLieuVaiJoin.get("id").in(filterRequest.getChatLieuVai()));
        }

        // Lọc theo thương hiệu (thuongHieu)
        if (filterRequest.getThuongHieu() != null) {
            predicates.add(thuongHieuJoin.get("id").in(filterRequest.getThuongHieu()));
        }

        // Lọc theo khoảng giá trong ProductDetail (giaBan)
        if (filterRequest.getMinPrice() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(productDetailJoin.get("giaBan"), filterRequest.getMinPrice()));
        }
        if (filterRequest.getMaxPrice() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(productDetailJoin.get("giaBan"), filterRequest.getMaxPrice()));
        }



        // Trả về Predicate kết hợp các điều kiện
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
