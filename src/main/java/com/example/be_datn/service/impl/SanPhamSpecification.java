package com.example.be_datn.service.impl;

import com.example.be_datn.entity.SanPham;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;


public class SanPhamSpecification {

    public static Specification<SanPham> activeUsers() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("trangThai"), 1);
    }

    public static Specification<SanPham> byDanhMuc(List<Long> idDanhMucs) {
        return (root, query, criteriaBuilder) -> {
            if (idDanhMucs == null || idDanhMucs.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Join<Object, Object> danhMucJoin = root.join("danhMuc");

            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("trangThai"), 1),
                    danhMucJoin.get("id").in(idDanhMucs)
            );
        };
    }

    public static Specification<SanPham> byNameSanPham(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("tenSanPham"), "%" + name + "%");
    }
}
