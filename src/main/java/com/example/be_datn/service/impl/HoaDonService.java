package com.example.be_datn.service.impl;

import com.example.be_datn.dto.Response.HoaDonResponse;
import com.example.be_datn.mapper.HoaDonMapper;
import com.example.be_datn.repository.HoaDonRepository;
import com.example.be_datn.service.IHoaDonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HoaDonService implements IHoaDonService {
    HoaDonRepository hoaDonRepository;
    HoaDonMapper hoaDonMapper;

    @Override
    public HoaDonResponse findByMaHoaDon(String maHoaDon) {
        return hoaDonMapper.toHoaDonResponse(hoaDonRepository.findByMaHoaDon(maHoaDon));
    }
}
