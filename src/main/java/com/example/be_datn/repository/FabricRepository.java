package com.example.be_datn.repository;

import com.example.be_datn.entity.Fabric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FabricRepository extends JpaRepository<Fabric, Integer> {
    Optional<Fabric> findByName(String name);
}