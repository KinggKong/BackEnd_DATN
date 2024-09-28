package com.example.be_datn.Repository;


import com.example.be_datn.Entity.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SizeRepository extends JpaRepository<Size, Long> {

    boolean existsSizeByTenSize(String tenSize);

    @Query(value = "select s from Size s where s.tenSize like :tenSize")
    Page<Size> findSizeByTenSizeLike(String tenSize, Pageable pageable);


}
