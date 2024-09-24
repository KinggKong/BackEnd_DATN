package com.example.be_datn.Repository;

import com.example.be_datn.Entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MauSacRepository extends JpaRepository<MauSac, Long> {
    boolean existsMauSacByTenMau(String tenMau);
}
