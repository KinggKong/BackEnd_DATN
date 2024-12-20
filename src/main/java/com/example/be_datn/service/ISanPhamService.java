package com.example.be_datn.service;

import com.example.be_datn.dto.Request.SanPhamFilterRequest;
import com.example.be_datn.dto.Request.SanPhamRequest;
import com.example.be_datn.dto.Response.SanPhamCustumerResponse;
import com.example.be_datn.dto.Response.SanPhamResponse;
import com.example.be_datn.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISanPhamService {
    Page<SanPhamResponse> getAllPageable(Pageable pageable);
    Page<SanPham> getAllPageableCustumer(Pageable pageable);
    Page<SanPhamCustumerResponse> getAllPageableCustumerFilter(List<Long> danhMucs,List<Long> thuongHieus,List<Long> chatLieuDes,List<Long> chatLieuVai,String ten,Double min,Double max,String sortBy,Pageable pageable);
    List<SanPhamResponse> getAllByTenSanPhamContaning(String tenSanPham);
    List<SanPhamCustumerResponse> getSanPhamByDanhMucID(Integer id);
    SanPhamResponse updateStatus(Long idSanPham ,int status);

    Page<SanPhamResponse> getAllWithFilter(Long idDanhMuc, Long idThuongHieu, Long idChatLieuVai, Long idChatLieuDe, String tenSanPham, Pageable pageable);

    SanPhamResponse getById(Long id);

    SanPhamResponse create(SanPhamRequest sanPhamRequest);

    SanPhamResponse update(SanPhamRequest sanPhamRequest, Long id);

    String delete(Long id);

    List<SanPham> filterProducts(SanPhamFilterRequest filterRequest);
    List<SanPhamCustumerResponse> listSanPhamGiamGia();
    List<SanPhamCustumerResponse> listSanPhamBanChay();
    List<SanPhamResponse> listSanPhamMoi();
}
