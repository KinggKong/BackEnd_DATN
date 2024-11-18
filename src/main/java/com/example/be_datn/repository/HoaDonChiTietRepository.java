package com.example.be_datn.repository;

import com.example.be_datn.dto.Response.HoaDonChiTietResponse;
import com.example.be_datn.entity.HoaDon;
import com.example.be_datn.entity.HoaDonChiTiet;
import com.example.be_datn.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Long> {
    @Query(
            """
            select new com.example.be_datn.dto.Response.HoaDonChiTietResponse(
                hdct.id, hd.id,spct.id, spct.sanPham.tenSanPham, hdct.soLuong, hdct.created_at,
                hdct.updated_at, hdct.tongTien, hdct.trangThai
            )
            from HoaDonChiTiet hdct 
            join hdct.hoaDon hd 
            join hdct.sanPhamChiTiet spct
            """
    )
    Page<HoaDonChiTietResponse> getAllHdct(Pageable pageable);


    @Query(
            """
                        select new com.example.be_datn.dto.Response.HoaDonChiTietResponse(
                            hdct.id, hd.id,spct.id, spct.sanPham.tenSanPham, hdct.soLuong, hdct.created_at,
                            hdct.updated_at, hdct.tongTien, hdct.trangThai
                        ) from HoaDonChiTiet hdct join HoaDon hd on hdct.hoaDon.id = hd.id
                        join SanPhamChiTiet spct on spct.id = hdct.sanPhamChiTiet.id where hd.id=:hoaDonId
                    """
    )
    List<HoaDonChiTietResponse> getAllHdctByIdHoaDon(Long hoaDonId);

    Optional<HoaDonChiTiet> findByHoaDonAndSanPhamChiTiet(HoaDon hoaDon, SanPhamChiTiet sanPhamChiTiet);
}
