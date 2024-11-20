package com.example.be_datn.repository;

import com.example.be_datn.entity.SanPham;
import jakarta.persistence.criteria.Join;
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

    public static Specification<SanPham> byThuongHieu(Long idThuongHieu) {
        return (root, query, criteriaBuilder) -> {
            if (idThuongHieu == null) {
                return criteriaBuilder.conjunction();
            }

            Join<Object, Object> thuongHieuJoin = root.join("thuongHieu");

            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("trangThai"), 1),
                    criteriaBuilder.equal(thuongHieuJoin.get("id"), idThuongHieu)
            );
        };
    }

    public static Specification<SanPham> byChatLieuDe(List<Long> idChatLieuDes) {
        return (root, query, criteriaBuilder) -> {
            if (idChatLieuDes == null || idChatLieuDes.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Join<Object, Object> danhMucJoin = root.join("chatLieuDe");

            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("trangThai"), 1),
                    danhMucJoin.get("id").in(idChatLieuDes)
            );
        };
    }

    public static Specification<SanPham> byChatLieuVai(List<Long> idChatLieuVais) {
        return (root, query, criteriaBuilder) -> {
            if (idChatLieuVais == null || idChatLieuVais.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Join<Object, Object> danhMucJoin = root.join("chatLieuVai");

            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("trangThai"), 1),
                    danhMucJoin.get("id").in(idChatLieuVais)
            );
        };
    }

    public static Specification<SanPham> byNameSanPham(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("tenSanPham"), "%" + name + "%");
    }
}
