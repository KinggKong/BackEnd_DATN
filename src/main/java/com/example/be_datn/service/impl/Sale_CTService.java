package com.example.be_datn.service.impl;

import com.example.be_datn.entity.SaleCt;
import com.example.be_datn.repository.Sale_ChiTietRepository;
import com.example.be_datn.service.ISale_CTService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class Sale_CTService implements ISale_CTService {
    Sale_ChiTietRepository sale_ChiTietRepository;
    @Override
    public SaleCt createSaleCt(SaleCt saleCt) {
        return sale_ChiTietRepository.save(saleCt);
    }
}
