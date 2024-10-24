package com.example.be_datn.service;

import com.example.be_datn.dto.Request.SanPhamChiTietRequest;
import com.example.be_datn.dto.Response.SanPhamChiTietResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface ISanPhamChiTietService {
    Page<SanPhamChiTietResponse> getAllPage(Pageable pageable);
    Page<SanPhamChiTietResponse> getAllPageBySanPhamId(Long id, Pageable pageable);
    Page<SanPhamChiTietResponse> getAllByFilter(Long idDanhMuc, Long idThuongHieu, Long idChatLieuVai, Long idChatLieuDe, Long idSanPham, Pageable pageable);
    List<SanPhamChiTietResponse> create(List<SanPhamChiTietRequest> sanPhamChiTietRequest) throws IOException;
    SanPhamChiTietResponse update(Long id, SanPhamChiTietRequest sanPhamChiTietRequest);
    SanPhamChiTietResponse getById(Long id);
    String delete(Long id);
    SanPhamChiTietResponse updateStatus(Long idSanPhamChiTiet, int status);
    SanPhamChiTietResponse getSPCTByMauSacAndKichThuoc(Long idSp,Long idMauSac, Long idKichThuoc);

}
