package com.example.be_datn.service.impl;

import com.example.be_datn.entity.MauSac;
import com.example.be_datn.Exception.AppException;
import com.example.be_datn.Exception.ErrorCode;
import com.example.be_datn.repository.MauSacRepository;
import com.example.be_datn.service.IMauSacService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class MauSacSerrvice implements IMauSacService {
    MauSacRepository mauSacRepository;

    @Override
    public List<MauSac> getAllMauSac() {
        return mauSacRepository.findAll();
    }

    @Override
    public Page<MauSac> getAllMauSacPageable(String ten_mau, Pageable pageable) {
        return mauSacRepository.findMauSacByTenMauLike("%" + ten_mau + "%", pageable);
    }

    @Override
    public MauSac createMauSac(MauSac mauSac) {
        if (mauSacRepository.existsMauSacByTenMau(mauSac.getTenMau())) {
            throw new AppException(ErrorCode.MAUSAC_ALREADY_EXISTS);
        }
        return mauSacRepository.save(mauSac);
    }

    @Override
    public MauSac getMauSacById(Long id) {
        return mauSacRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MAUSAC_NOT_FOUND));
    }

    @Override
    public MauSac updateMauSac(Long idMauSac, MauSac mauSac) {
        Optional<MauSac> mauSacOptional = mauSacRepository.findById(idMauSac);
        if (mauSacOptional.isPresent()) {
            MauSac mauSacUpdate = mauSacOptional.get();
            mauSacUpdate.setTenMau(mauSac.getTenMau());
            mauSacUpdate.setTrangThai(mauSac.getTrangThai());
            mauSacUpdate.setUpdated_at(mauSac.getUpdated_at());
            return mauSacRepository.save(mauSacUpdate);
        } else {
            throw new AppException(ErrorCode.MAUSAC_NOT_FOUND);
        }
    }

    @Override
    public String deleteMauSac(Long id) {
        getMauSacById(id);
        mauSacRepository.deleteById(id);
        return "deleted successfull";
    }

}
