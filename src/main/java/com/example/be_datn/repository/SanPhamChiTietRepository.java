package com.example.be_datn.repository;

import com.example.be_datn.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet,Long> {
    @Query(value = "select * from san_pham_chi_tiet where id_san_pham = ?1 and id_mau_sac = ?2 and id_kich_thuoc = ?3", nativeQuery = true)
    SanPhamChiTiet findBySanPhamIdAndMauSacIdAndKichThuocId(Long idSanPham, Long idMauSac, Long idKichThuoc);
    @Query("select spct from SanPhamChiTiet spct join SanPham sp on spct.sanPham.id = sp.id where sp.id = ?1 and spct.trangThai = 1")
    Page<SanPhamChiTiet> findBySanPhamId(Long id, Pageable pageable);

//    Page<SanPhamChiTiet> getAll(Pageable pageable);
}
