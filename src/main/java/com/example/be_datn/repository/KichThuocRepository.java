
package com.example.be_datn.repository;

import com.example.be_datn.entity.KichThuoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KichThuocRepository extends JpaRepository<KichThuoc, Long> {
    boolean existsKichThuocByTenKichThuoc(String tenKichThuoc); // Phương thức kiểm tra sự tồn tại theo tên kích thước

    @Query(value = "select k from KichThuoc k where k.tenKichThuoc like :tenKichThuoc") // Query để tìm kích thước theo tên
    Page<KichThuoc> findKichThuocByTenKichThuocLike(String tenKichThuoc, Pageable pageable); // Phương thức tìm kích thước
}