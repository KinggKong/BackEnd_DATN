package com.example.be_datn.repository;

import com.example.be_datn.entity.HoaDon;
import com.example.be_datn.entity.HoaDonCT;
import com.example.be_datn.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HoaDonChiTietRepository extends JpaRepository<HoaDonCT, Long> {

//
//
//    @Query(
//            """
//                            select new com.example.be_datn.dto.Response.HoaDonCTResponse(
//                    hdct.id, hd.id,spct.id, spct.sanPham.tenSanPham, hdct.soLuong,spct.giaBan, hd.tongTien
//                            ) from HoaDonCT hdct join HoaDon hd on hdct.hoaDon.id = hd.id
//                             left join SanPhamChiTiet spct on spct.id = hdct.sanPhamChiTiet.id where hd.id=:hoaDonId
//                    """
//    )
//    List<HoaDonCTResponse> getAllHdctByIdHoaDon(Long hoaDonId);
    List<HoaDonCT> getAllByHoaDon_Id(Long id);

    Optional<HoaDonCT> findByHoaDonAndSanPhamChiTiet(HoaDon hoaDon, SanPhamChiTiet sanPhamChiTiet);

    List<HoaDonCT> findByHoaDon_Id(Long id);

    void deleteByHoaDon_Id(Long id);

    @Query("select hdct from HoaDonCT hdct where hdct.hoaDon.trangThai = 'PENDING' and hdct.hoaDon.id=:id")
    List<HoaDonCT> findHoaDonCTByTTPenDing(@Param("id") Long id);
}
