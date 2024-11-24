package com.example.be_datn.service.impl;

import com.example.be_datn.entity.SanPham;
import com.example.be_datn.entity.SanPhamChiTiet;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
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

    //    public static Specification<SanPham> byThuongHieu(Long idThuongHieu) {
//        return (root, query, criteriaBuilder) -> {
//            if (idThuongHieu == null) {
//                return criteriaBuilder.conjunction();
//            }
//
//            Join<Object, Object> thuongHieuJoin = root.join("thuongHieu");
//
//            return criteriaBuilder.and(
//                    criteriaBuilder.equal(root.get("trangThai"), 1),
//                    criteriaBuilder.equal(thuongHieuJoin.get("id"), idThuongHieu)
//            );
//        };
//    }
    public static Specification<SanPham> byThuongHieu(List<Long> idThuongHieus) {
        return (root, query, criteriaBuilder) -> {
            if (idThuongHieus == null || idThuongHieus.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Join<Object, Object> thuongHieuJoin = root.join("thuongHieu");

            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("trangThai"), 1),
                    thuongHieuJoin.get("id").in(idThuongHieus)
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

    public static Specification<SanPham> byMinPrice(Double minPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice != null && minPrice > 0) {
                Join<SanPham, SanPhamChiTiet> sanPhamChiTietJoin = root.join("sanPhamChiTietList");
                return criteriaBuilder.greaterThanOrEqualTo(sanPhamChiTietJoin.get("giaBan"), minPrice);
            }
            return criteriaBuilder.conjunction(); // Nếu không có điều kiện, trả về điều kiện hợp lệ
        };
    }

    public static Specification<SanPham> byMaxPrice(Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (maxPrice != null && maxPrice > 0) {
                Join<SanPham, SanPhamChiTiet> sanPhamChiTietJoin = root.join("sanPhamChiTietList");
//                return criteriaBuilder.lessThanOrEqualTo(sanPhamChiTietJoin.get("giaBan"), maxPrice);
                Predicate maxPriceCondition = criteriaBuilder.lessThanOrEqualTo(sanPhamChiTietJoin.get("giaBan"), maxPrice);
                // Sử dụng DISTINCT để chỉ lấy sản phẩm duy nhất nếu có nhiều chi tiết thỏa mãn điều kiện
                query.distinct(true);
                return maxPriceCondition;
            }
            return criteriaBuilder.conjunction(); // Nếu không có điều kiện, trả về điều kiện hợp lệ
        };
    }

    // Sắp xếp theo giá từ cao đến thấp
    public static Specification<SanPham> sortByPriceDesc() {
        return (root, query, criteriaBuilder) -> {
            Join<SanPham, SanPhamChiTiet> sanPhamChiTietJoin = root.join("sanPhamChiTietList");

            query.orderBy(criteriaBuilder.desc(sanPhamChiTietJoin.get("giaBan")));
            return criteriaBuilder.conjunction(); // Trả về điều kiện hợp lệ
        };
    }

    // Sắp xếp theo giá từ thấp đến cao
    public static Specification<SanPham> sortByPriceAsc() {
        return (root, query, criteriaBuilder) -> {
            Join<SanPham, SanPhamChiTiet> sanPhamChiTietJoin = root.join("sanPhamChiTietList");
            query.orderBy(criteriaBuilder.asc(sanPhamChiTietJoin.get("giaBan")));
            return criteriaBuilder.conjunction(); // Trả về điều kiện hợp lệ
        };
    }

    public static Specification<SanPham> byPriceAndSort(
            Double minPrice, Double maxPrice, boolean sortByAsc) {
        return (root, query, criteriaBuilder) -> {
            // Tham gia bảng sanPhamChiTietList
            Join<SanPham, SanPhamChiTiet> sanPhamChiTietJoin = root.join("sanPhamChiTietList");

            // Xây dựng các điều kiện giá
            Predicate predicate = criteriaBuilder.conjunction();
            if (minPrice != null && minPrice > 0) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(sanPhamChiTietJoin.get("giaBan"), minPrice));
            }
            if (maxPrice != null && maxPrice > 0) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.lessThanOrEqualTo(sanPhamChiTietJoin.get("giaBan"), maxPrice));
            }

            // Sắp xếp giá
            if (sortByAsc) {
                query.orderBy(criteriaBuilder.asc(sanPhamChiTietJoin.get("giaBan")));
            } else {
                query.orderBy(criteriaBuilder.desc(sanPhamChiTietJoin.get("giaBan")));
            }

            // Đảm bảo chỉ lấy sản phẩm duy nhất
            query.distinct(true);

            return predicate;
        };
    }


    // Sắp xếp theo ngày tạo mới nhất
    public static Specification<SanPham> sortByNewest() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("created_at"))); // Giả sử "ngayTao" là trường ngày tạo
            return criteriaBuilder.conjunction(); // Trả về điều kiện hợp lệ
        };
    }


}
