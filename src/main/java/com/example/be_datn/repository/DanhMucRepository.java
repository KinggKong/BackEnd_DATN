package com.example.be_datn.repository;

import com.example.be_datn.entity.DanhMuc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DanhMucRepository extends JpaRepository<DanhMuc, Long> {
    boolean existsDanhMucByTenDanhMuc(String tenDanhMuc);

    @Query(value = "select h from DanhMuc h where h.tenDanhMuc like :tenDanhMuc")
    Page<DanhMuc> findHangByHangLike(String tenDanhMuc, Pageable pageable);
}