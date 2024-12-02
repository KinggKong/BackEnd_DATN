package com.example.be_datn.repository;


import com.example.be_datn.entity.ViaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ViaCodeRepository extends JpaRepository<ViaCode,Long> {
    @Query(value = "select v from ViaCode v where v.email = :email order by v.createdAt desc limit 1")
    ViaCode findByEmail(@Param("email") String email);
}
