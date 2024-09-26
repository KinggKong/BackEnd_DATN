package com.example.be_datn.Repository;

import com.example.be_datn.Entity.ThuongHieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ThuongHieuRepository extends JpaRepository<ThuongHieu, Long> {
    boolean existsThuongHieuByTenThuongHieu(String tenMau);

    @Query(value = "select th from ThuongHieu th where th.tenThuongHieu like :tenThuongHieu")
    Page<ThuongHieu> findThuongHieuByTenThuongHieuLike(String tenThuongHieu, Pageable pageable);
}
