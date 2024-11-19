package com.example.be_datn.service;

import com.example.be_datn.entity.SaleCt;

public interface ISale_CTService {
    SaleCt createSaleCt(SaleCt saleCt);
    SaleCt getSaleCtById(Long id);
    SaleCt getSaleCtByIdSanPhamCt(Long idSanPhamChiTiet);
}
