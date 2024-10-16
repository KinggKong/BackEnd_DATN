package com.example.be_datn.repository;

import com.example.be_datn.entity.HinhAnh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HinhAnhRepository extends JpaRepository<HinhAnh, Long> {
    @Query(value = "select * from hinh_anh where src = ?1", nativeQuery = true)
    HinhAnh findBySanPhamChiTietId(String src);

    @Query(value = "select * from hinh_anh where id_san_pham_ct = ?1", nativeQuery = true)
    List<HinhAnh> findByIdSanPhamCt(Long idSanPhamCt);

    @Query(value = "select * from hinh_anh where url = ?1", nativeQuery = true)
    HinhAnh findByUrl(String url);
}
