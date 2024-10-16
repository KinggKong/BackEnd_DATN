package com.example.be_datn.service;

import com.example.be_datn.entity.HinhAnh;
import com.example.be_datn.entity.SanPhamChiTiet;

import java.util.List;

public interface IHinhAnhService {
   void addHinhAnh(String url,Long idSanPhamCt, SanPhamChiTiet sanPhamChiTiet);
   List<HinhAnh> getAll();
   List<HinhAnh> getAllByIdSanPhamCt(Long idSanPhamCt);
   void deleteHinhAnhByUrl(String url);
}
