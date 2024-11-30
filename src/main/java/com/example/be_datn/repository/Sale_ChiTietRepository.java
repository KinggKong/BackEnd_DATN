package com.example.be_datn.repository;

import com.example.be_datn.entity.SaleCt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface Sale_ChiTietRepository extends JpaRepository<SaleCt,Long> {
    @Query("select s from SaleCt s where s.idSanPhamCt = ?1 and s.sale.id = ?2")
    SaleCt findByIdSanPhamCtAndIdSale(Long idSanPhamChiTiet, Long id);
}
