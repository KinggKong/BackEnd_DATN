package com.example.be_datn.repository;

import com.example.be_datn.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, String> {
    List<District> findByProvince_Code(String province);
}
