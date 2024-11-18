package com.example.be_datn.repository;

import com.example.be_datn.dto.Response.HoaDonResponse;
import com.example.be_datn.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {
    @Query("""
                   select new com.example.be_datn.dto.Response.HoaDonResponse(
                    hd.id,
                    hd.maHoaDon, hd.tenNguoiNhan, hd.diaChiNhan, hd.sdt, hd.tongTien,
                    hd.tienSauGiam, hd.tienShip, hd.ghiChu, hd.loaiHoaDon, hd.email,
                    nv.ten, kh.ten, hd.hinhThucThanhToan, v.maVoucher, hd.soTienGiam,
                    hd.soLuong,
                     hd.trangThai
                   ) from HoaDon hd 
                   left join hd.nhanVien nv 
                   left join hd.khachHang kh 
                   left join hd.voucher v  where hd.trangThai = 1
            """)
    Page<HoaDonResponse> findAllHoaDon(Pageable pageable);


    @Transactional
    @Modifying
    @Query(
            """
                        update HoaDon hd  set hd.tongTien=:tongTien, hd.soLuong=:soLuong where hd.id=:id
                    """
    )
    void updateHoaDon(Double tongTien,Integer soLuong, Long id);

}
