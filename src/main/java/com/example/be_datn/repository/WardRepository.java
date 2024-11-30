package com.example.be_datn.repository;

import com.example.be_datn.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WardRepository extends JpaRepository<Ward, String> {
    List<Ward> findByDistrict_Code(String district);
}
