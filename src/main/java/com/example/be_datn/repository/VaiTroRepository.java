package com.example.be_datn.repository;

import com.example.be_datn.entity.VaiTro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaiTroRepository extends JpaRepository<VaiTro, Long> {
    VaiTro findByTenVaiTro(String tenVaiTro);
}
