package com.example.be_datn.repository;

import com.example.be_datn.dto.Response.HoaDonResponse;
import com.example.be_datn.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {
    @Query("""
               select new com.example.be_datn.dto.Response.HoaDonResponse(
                hd.id,
                hd.maHoaDon, hd.tenNguoiNhan, hd.diaChiNhan, hd.sdt, hd.tongTien,
                hd.tienSauGiam, hd.tienShip, hd.ghiChu, hd.loaiHoaDon, hd.email,
                nv.ten, kh.ten, hd.hinhThucThanhToan, hd.trangThai, v.maVoucher, hd.soTienGiam,
               hd.created_at, hd.updated_at
               ) from HoaDon hd 
               left join hd.nhanVien nv 
               left join hd.khachHang kh 
               left join hd.voucher v  
               where hd.trangThai = 'WAITING' or hd.trangThai = 'PENDING'
            """)
    Page<HoaDonResponse> findAllHoaDon(Pageable pageable);


    @Transactional
    @Modifying
    @Query(
            """
                        update HoaDon hd  set hd.tongTien=:tongTien where hd.id=:id
                    """
    )
    void updateHoaDon(Double tongTien, Long id);

    HoaDon findByMaHoaDon(String maHoaDon);

    @Query("SELECT hd FROM HoaDon hd " +
            "LEFT JOIN hd.khachHang kh " +
            "WHERE hd.trangThai LIKE CONCAT('%', :trangThai, '%') " +
            "ORDER BY hd.created_at DESC")
    List<HoaDon> findByTrangThai(@Param("trangThai") String trangThai);

    @Query("""
               select hd from HoaDon hd where hd.trangThai = 'PENDING'
            """)
    List<HoaDon> selectOrderWhereStatusIsPending();




}
