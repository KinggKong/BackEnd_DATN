package com.example.be_datn.repository;

import com.example.be_datn.entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MauSacRepository extends JpaRepository<MauSac, Long> {
    boolean existsMauSacByTenMau(String tenMau);
}
