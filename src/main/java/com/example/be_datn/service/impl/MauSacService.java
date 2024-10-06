package com.example.be_datn.service.impl;


import com.example.be_datn.dto.Request.MauSacRequest;
import com.example.be_datn.dto.Response.MauSacResponse;
import com.example.be_datn.entity.MauSac;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.MauSacRepository;
import com.example.be_datn.service.IMauSacService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class MauSacService implements IMauSacService {
    MauSacRepository mauSacRepository;

    @Override
    public Page<MauSacResponse> getAllMauSacPageable(String tenMau, Pageable pageable) {
        return mauSacRepository.findMauSacByTenMauLike("%" + tenMau + "%", pageable).map(MauSacResponse::fromMauSac);
    }

    @Override
    public MauSacResponse createMauSac(MauSacRequest mauSacRequest) {
        if (mauSacRepository.existsMauSacByTenMau(mauSacRequest.getTenMau())) {
            throw new AppException(ErrorCode.MAUSAC_ALREADY_EXISTS);
        }
        MauSac mauSac = MauSac.builder()
                .tenMau(mauSacRequest.getTenMau())
                .trangThai(mauSacRequest.getTrangThai())
                .build();
        MauSac savedMauSac = mauSacRepository.save(mauSac);
        return MauSacResponse.fromMauSac(savedMauSac);
    }

    @Override
    public MauSacResponse getMauSacById(Long id) {
        return mauSacRepository.findById(id).map(MauSacResponse::fromMauSac).orElseThrow(() -> new AppException(ErrorCode.MAUSAC_NOT_FOUND));
    }

    @Override
    public MauSacResponse updateMauSac(Long idMauSac, MauSacRequest mauSacRequest) {
        var mauSac = mauSacRepository.findById(idMauSac).orElseThrow(() -> new AppException(ErrorCode.MAUSAC_NOT_FOUND));
        mauSac.setTenMau(mauSacRequest.getTenMau());
        mauSac.setTrangThai(mauSacRequest.getTrangThai());
        return MauSacResponse.fromMauSac(mauSacRepository.save(mauSac));
    }

    @Override
    public String deleteMauSac(Long id) {
        getMauSacById(id);
        mauSacRepository.deleteById(id);
        return "deleted successfully";
    }

}
