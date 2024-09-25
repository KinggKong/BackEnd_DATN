package com.example.be_datn.Repository;

import com.example.be_datn.Entity.MauSac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MauSacRepository extends JpaRepository<MauSac, Long> {
    boolean existsMauSacByTenMau(String tenMau);

    @Query(value = "select m from MauSac m where m.tenMau like :tenMau")
    Page<MauSac> findMauSacByTenMauLike(String tenMau, Pageable pageable);
}
