package com.example.be_datn.service;

import com.example.be_datn.entity.SanPhamChiTiet;

public interface IHinhAnhService {
   void addHinhAnh(String url,Long idSanPhamCt, SanPhamChiTiet sanPhamChiTiet);
}
