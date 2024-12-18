package com.example.be_datn.repository;

import com.example.be_datn.dto.Response.HistoryBillResponse;
import com.example.be_datn.dto.Response.HoaDonResponse;
import com.example.be_datn.entity.HoaDon;
import com.example.be_datn.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {
    @Query("""
               select new com.example.be_datn.dto.Response.HoaDonResponse(
                hd.id,
                hd.maHoaDon, hd.tenNguoiNhan, hd.diaChiNhan, hd.sdt, hd.tienSauGiam,
                hd.tienSauGiam, hd.tienShip, hd.ghiChu, hd.loaiHoaDon, hd.email,
                nv.ten, kh.ten, hd.hinhThucThanhToan, hd.trangThai, v.maVoucher, hd.soTienGiam,
               hd.created_at, hd.updated_at, v.id
               ) from HoaDon hd 
               left join hd.nhanVien nv 
               left join hd.khachHang kh 
               left join hd.voucher v  
               where  hd.trangThai = 'PENDING' and hd.loaiHoaDon = 'OFFLINE' or (hd.loaiHoaDon = 'ONLINE' AND hd.trangThai = 'PENDING')

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

    @Query("SELECT hd FROM HoaDon hd WHERE (hd.tenNguoiNhan like CONCAT('%', :keySearch, '%')  or hd.sdt like CONCAT('%', :keySearch, '%') or hd.maHoaDon like CONCAT('%', :keySearch, '%') or hd.email like  CONCAT('%', :keySearch, '%') ) " +
            "AND hd.trangThai LIKE CONCAT('%', :trangThai, '%')  " +
            "AND hd.trangThai <> 'PENDING'  " +
            "order by hd.created_at desc ")
    List<HoaDon> findByTrangThai(@Param("trangThai") String trangThai, @Param("keySearch") String keySearch);



    @Query("""
               select new com.example.be_datn.dto.Response.HistoryBillResponse(
                hd.id,
                hd.maHoaDon, hd.tenNguoiNhan, hd.diaChiNhan, hd.sdt, hd.tongTien,
                hd.tienSauGiam, hd.tienShip, hd.ghiChu, hd.loaiHoaDon, hd.email,
                nv.ten, kh.ten, hd.hinhThucThanhToan, hd.trangThai, v.maVoucher, hd.soTienGiam,
               hd.created_at, hd.updated_at
               ) from HoaDon hd 
               left join hd.nhanVien nv 
               left join hd.khachHang kh 
               left join hd.voucher v  
               where hd.khachHang.id =:idKhachHang
            """)
    List<HistoryBillResponse> getAllHistoryBillByIdKhachHang(@Param("idKhachHang") Long idKhachHang);



    @Query("""
               select hd from HoaDon hd where hd.trangThai = 'PENDING'
            """)
    List<HoaDon> selectOrderWhereStatusIsPending();


    @Query(value = """
            SELECT COUNT(*) 
            FROM hoa_don hdb 
            WHERE hdb.trang_thai = :trangThai 
              AND hdb.created_at >= :ngayBatDau 
              AND hdb.created_at <= :ngayKetThuc 
              AND ( :loaiHoaDon ='' OR hdb.loai_hoa_don = :loaiHoaDon or  :loaiHoaDon IS NULL)
            """, nativeQuery = true)
    int countByTrangThai(@Param("trangThai") String trangThai,
                         @Param("ngayBatDau") LocalDateTime ngayBatDau,
                         @Param("ngayKetThuc") LocalDateTime ngayKetThuc,
                         @Param("loaiHoaDon") String loaiHoaDon);

    @Query("select sum(hdb.tongTien) from HoaDon hdb where hdb.trangThai='DONE' and hdb.created_at >= ?1 and hdb.created_at <= ?2")
    Float tongDoanhThu(LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc);

    @Query(value = """
            SELECT SUM(hdb.tien_sau_giam - hdb.tien_ship) 
            FROM hoa_don hdb 
            WHERE hdb.trang_thai = 'DONE' 
              AND hdb.created_at >= :ngayBatDau 
              AND hdb.created_at <= :ngayKetThuc 
              AND ( :loaiHoaDon ='' OR hdb.loai_hoa_don = :loaiHoaDon or  :loaiHoaDon IS NULL)
            """, nativeQuery = true)
    Float tongDoanhThuNative(@Param("ngayBatDau") LocalDateTime ngayBatDau,
                             @Param("ngayKetThuc") LocalDateTime ngayKetThuc,
                             @Param("loaiHoaDon") String loaiHoaDon);

    @Query("select sum(hdct.soLuong) from HoaDonCT hdct join HoaDon hd on hdct.hoaDon.id = hd.id where hd.trangThai ='DONE' and  hd.created_at >= ?1 and hd.created_at <= ?2")
    Integer tongSanPhamBan(LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc);



    @Query(value = """
            SELECT SUM(hdct.so_luong) 
            FROM hoa_don_ct hdct 
            JOIN hoa_don hd ON hdct.id_hoa_don = hd.id 
            WHERE hd.trang_thai = 'DONE' 
              AND hd.created_at >= :ngayBatDau 
              AND hd.created_at <= :ngayKetThuc
             AND (:loaiHoaDon ='' OR hd.loai_hoa_don = :loaiHoaDon or  :loaiHoaDon IS NULL)
            """, nativeQuery = true)
    Integer tongSanPhamBanNative(@Param("ngayBatDau") LocalDateTime ngayBatDau,
                                 @Param("ngayKetThuc") LocalDateTime ngayKetThuc,
                                 @Param("loaiHoaDon") String loaiHoaDon);


    @Query("SELECT new map(FUNCTION('DATE_FORMAT', hd.created_at, '%Y-%m-%d') as ngay, SUM(hd.tongTien) as doanhThu) " +
            "FROM HoaDon hd " +
            "WHERE hd.trangThai = 'DONE' AND hd.created_at >= :ngayBatDau AND hd.created_at <= :ngayKetThuc " +
            "GROUP BY FUNCTION('DATE_FORMAT', hd.created_at, '%Y-%m-%d') " +
            "ORDER BY ngay ASC")
    List<Map<String, Object>> doanhThuTheoThoiGian(@Param("ngayBatDau") LocalDateTime ngayBatDau,
                                                   @Param("ngayKetThuc") LocalDateTime ngayKetThuc);

    //Query theo ngày trong thang
    @Query(value = "SELECT " +
            "  all_dates.ngay, " +
            "  COALESCE(SUM(hd.tien_sau_giam - hd.tien_ship), 0) AS doanhThu " +
            "FROM (SELECT DATE(:monthDate) + INTERVAL a DAY AS ngay " +
            "      FROM (SELECT 0 AS a UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL " +
            "            SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL " +
            "            SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 " +
            "            UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15 UNION ALL SELECT 16 UNION ALL SELECT 17 " +
            "            UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20 UNION ALL SELECT 21 UNION ALL SELECT 22 " +
            "            UNION ALL SELECT 23 UNION ALL SELECT 24 UNION ALL SELECT 25 UNION ALL SELECT 26 UNION ALL SELECT 27 " +
            "            UNION ALL SELECT 28 UNION ALL SELECT 29 UNION ALL SELECT 30 UNION ALL SELECT 31) days) all_dates " +
            "LEFT JOIN hoa_don hd ON DATE(hd.created_at) = all_dates.ngay AND hd.trang_thai = 'DONE' " +
            "WHERE all_dates.ngay BETWEEN DATE_FORMAT(:monthDate, '%Y-%m-01') AND LAST_DAY(:monthDate) " +
            "GROUP BY all_dates.ngay " +
            "ORDER BY all_dates.ngay ASC", nativeQuery = true)
    List<Map<String, Object>> getRevenueByDayInMonth(@Param("monthDate") LocalDateTime monthDate);

    @Query(value = "SELECT " +
            "  all_dates.ngay, " +
            "  COALESCE(SUM(hd.tien_sau_giam - hd.tien_ship), 0) AS doanhThu " +
            "FROM (SELECT DATE(:startDate) + INTERVAL a DAY AS ngay " +
            "      FROM (SELECT 0 AS a UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL " +
            "            SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL " +
            "            SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 " +
            "            UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15 UNION ALL SELECT 16 UNION ALL SELECT 17 " +
            "            UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20 UNION ALL SELECT 21 UNION ALL SELECT 22 " +
            "            UNION ALL SELECT 23 UNION ALL SELECT 24 UNION ALL SELECT 25 UNION ALL SELECT 26 UNION ALL SELECT 27 " +
            "            UNION ALL SELECT 28 UNION ALL SELECT 29 UNION ALL SELECT 30 UNION ALL SELECT 31) days) all_dates " +
            "LEFT JOIN hoa_don hd ON DATE(hd.created_at) = all_dates.ngay AND hd.trang_thai = 'DONE' " +
            " AND (:typeSale IS NULL OR :typeSale = '' OR hd.loai_hoa_don = :typeSale)" +
            "WHERE all_dates.ngay BETWEEN :startDate AND :endDate " +
            "GROUP BY all_dates.ngay " +
            "ORDER BY all_dates.ngay ASC", nativeQuery = true)
    List<Map<String, Object>> getRevenueByDayInPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("typeSale") String typeSale);

    //Tong doang thu theo các tháng trong năm

    @Query(value = """
            WITH danh_sach_thang AS (
                SELECT DATE_FORMAT(DATE(:startDate) + INTERVAL a MONTH, '%Y-%m') AS ngay
                FROM (SELECT 0 AS a UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL 
                             SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL 
                             SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11) months
            )
            SELECT 
                dst.ngay, 
                COALESCE(SUM(hdct.so_luong * hdct.gia_tien), 0) AS doanhThu
            FROM 
                danh_sach_thang dst
            LEFT JOIN hoa_don hd 
                ON DATE_FORMAT(hd.created_at, '%Y-%m') = dst.ngay
                AND hd.trang_thai = 'DONE'
                AND (:typeSale IS NULL OR :typeSale = '' OR hd.loai_hoa_don = :typeSale)
            LEFT JOIN hoa_don_ct hdct 
                ON hd.id = hdct.id_hoa_don
            GROUP BY 
                dst.ngay
            ORDER BY 
                dst.ngay ASC
            """, nativeQuery = true)
    List<Map<String, Object>> getDoanhThuTheoThang(@Param("startDate") LocalDateTime startDate, @Param("typeSale") String typeSale);

    //Query theo ngày trong thang của từng sản phẩm
    @Query(value = "SELECT " +
            "    all_dates.ngay, " +
            "    hdct.id_san_pham_ct, " +
            "    sp.ten_san_pham, " +
            "    COALESCE(SUM(hdct.so_luong * hdct.gia_tien), 0) AS doanhThu " +
            "FROM " +
            "    (SELECT DATE(:startDate) + INTERVAL a DAY AS ngay " +
            "     FROM (SELECT 0 AS a UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL " +
            "           SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL " +
            "           SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 " +
            "           UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15 " +
            "           UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 " +
            "           UNION ALL SELECT 19 UNION ALL SELECT 20 UNION ALL SELECT 21 UNION ALL SELECT 22 UNION ALL SELECT 23 " +
            "           UNION ALL SELECT 24 UNION ALL SELECT 25 UNION ALL SELECT 26 UNION ALL SELECT 27 " +
            "           UNION ALL SELECT 28 UNION ALL SELECT 29 UNION ALL SELECT 30 UNION ALL SELECT 31) days) all_dates " +
            "LEFT JOIN hoa_don hd " +
            "    ON DATE(hd.created_at) = all_dates.ngay " +
            "    AND hd.trang_thai = 'DONE' " +
            "   AND (:typeSale IS NULL OR :typeSale = '' OR hd.loai_hoa_don = :typeSale) " +
            "LEFT JOIN hoa_don_ct hdct " +
            "    ON hd.id = hdct.id_hoa_don " +
            "LEFT JOIN san_pham_chi_tiet spct " +
            "    ON hdct.id_san_pham_ct = spct.id " +
            "LEFT JOIN san_pham sp " +
            "    ON spct.id_san_pham = sp.id " +
            "WHERE " +
            "    all_dates.ngay BETWEEN :startDate AND :endDate " +
            "GROUP BY " +
            "    all_dates.ngay, hdct.id_san_pham_ct, sp.ten_san_pham " +
            "ORDER BY " +
            "    all_dates.ngay ASC, hdct.id_san_pham_ct ASC", nativeQuery = true)
    List<Map<String, Object>> getDoanhThuTheoSanPham(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("typeSale") String typeSale);

    // doanh thu cua từng sản phẩm của tháng trong năm
    @Query(value = """
                WITH danh_sach_thang AS (
                    SELECT DATE_FORMAT(DATE(:startDate) + INTERVAL a MONTH, '%Y-%m') AS ngay
                    FROM (SELECT 0 AS a UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL 
                                 SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL 
                                 SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11) months
                )
                SELECT 
                    dst.ngay, 
                    sp.ten_san_pham, 
                    COALESCE(SUM(hdct.so_luong * hdct.gia_tien), 0) AS doanhThu
                FROM 
                    danh_sach_thang dst
                LEFT JOIN hoa_don hd 
                    ON DATE_FORMAT(hd.created_at, '%Y-%m') = dst.ngay
                    AND hd.trang_thai = 'DONE'
                    AND (:typeSale IS NULL OR :typeSale = '' OR hd.loai_hoa_don = :typeSale)
                LEFT JOIN hoa_don_ct hdct 
                    ON hd.id = hdct.id_hoa_don
                LEFT JOIN san_pham_chi_tiet spct 
                    ON hdct.id_san_pham_ct = spct.id
                LEFT JOIN san_pham sp
                    ON spct.id_san_pham = sp.id
                GROUP BY 
                    dst.ngay,
                    sp.ten_san_pham
                ORDER BY 
                    dst.ngay ASC,
                    sp.ten_san_pham ASC
            """, nativeQuery = true)
    List<Map<String, Object>> getDoanhThuTheoSanPhamVaThang(@Param("startDate") LocalDateTime startDate, @Param("typeSale") String typeSale);

    //Lấy ra danh sách sản phẩm bán chạy theo doanh thu
    @Query(value = """
            SELECT
                sp.ten_san_pham AS tenSanPham,
                COALESCE(SUM(hdct.so_luong), 0) AS tongSoLuongBan,
                COALESCE(SUM(hdct.so_luong * hdct.gia_tien), 0) AS tongDoanhThu
            FROM
                hoa_don hd
            JOIN
                hoa_don_ct hdct ON hd.id = hdct.id_hoa_don
            JOIN
                san_pham_chi_tiet spct ON hdct.id_san_pham_ct = spct.id
            JOIN
                san_pham sp ON spct.id_san_pham = sp.id
            WHERE
                hd.trang_thai = 'DONE' -- Chỉ lấy hóa đơn đã hoàn thành
                AND hd.created_at >= :ngayBatDau -- Lọc theo ngày bắt đầu
                AND hd.created_at <= :ngayKetThuc -- Lọc theo ngày kết thúc
                AND (:loaiHoaDon ='' OR hd.loai_hoa_don = :loaiHoaDon or :loaiHoaDon IS NULL) -- Lọc theo loại hóa đơn nếu có
            GROUP BY
                sp.ten_san_pham
            ORDER BY
                tongDoanhThu DESC;
            """, nativeQuery = true)
    List<Object[]> findBestSellingProductsNative(@Param("ngayBatDau") LocalDateTime ngayBatDau,
                                                 @Param("ngayKetThuc") LocalDateTime ngayKetThuc,
                                                 @Param("loaiHoaDon") String loaiHoaDon);


    @Query(value = """
            SELECT COUNT(*) 
            FROM hoa_don hdb 
            WHERE hdb.trang_thai = :trangThai 
            """, nativeQuery = true)
    int countByTrangThaiAllTime(@Param("trangThai") String trangThai);

    @Query("SELECT h FROM HoaDon h WHERE h.voucher = :voucher")
    List<HoaDon> findByVoucher(@Param("voucher") Voucher voucher);

    List<HoaDon> findByTongTienBetweenAndVoucherIsNull(Double minTotalAmount, Double maxTotalAmount);

    @Query("""
    select hd from HoaDon hd 
    where (hd.trangThai = 'PENDING' and hd.loaiHoaDon = 'OFFLINE') 
    or (hd.loaiHoaDon = 'ONLINE' and hd.trangThai = 'PENDING')
    and hd.tongTien >= :tongTien
""")
    List<HoaDon> findHoaDonAndAddVoucher(double tongTien);

}
