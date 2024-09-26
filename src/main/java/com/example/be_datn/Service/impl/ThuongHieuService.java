package com.example.be_datn.Service.impl;

import com.example.be_datn.Entity.ThuongHieu;
import com.example.be_datn.Exception.AppException;
import com.example.be_datn.Exception.ErrorCode;
import com.example.be_datn.Repository.ThuongHieuRepository;
import com.example.be_datn.Service.IThuongHieuService;
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
public class ThuongHieuService implements IThuongHieuService {
    ThuongHieuRepository thuongHieuRepository;

    @Override
    public List<ThuongHieu> getAllThuongHieu() {
        return thuongHieuRepository.findAll();
    }

    @Override
    public Page<ThuongHieu> getAllThuongHieuPageable(String tenThuongHieu, Pageable pageable) {
        return thuongHieuRepository.findThuongHieuByTenThuongHieuLike("%" + tenThuongHieu + "%", pageable);
    }

    @Override
    public ThuongHieu createThuongHieu(ThuongHieu thuongHieu) {
        if (thuongHieuRepository.existsThuongHieuByTenThuongHieu(thuongHieu.getTenThuongHieu())) {
            throw new AppException(ErrorCode.THUONGHIEU_ALREADY_EXISTS);
        }
        return thuongHieuRepository.save(thuongHieu);
    }

    @Override
    public ThuongHieu getThuongHieuById(Long id) {
        return thuongHieuRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.THUONGHIEU_NOT_FOUND));
    }

    @Override
    public ThuongHieu updateThuongHieu(Long idThuongHieu, ThuongHieu ThuongHieu) {
        Optional<ThuongHieu> ThuongHieuOptional = thuongHieuRepository.findById(idThuongHieu);
        if (ThuongHieuOptional.isPresent()) {
            ThuongHieu ThuongHieuUpdate = ThuongHieuOptional.get();
            ThuongHieuUpdate.setTenThuongHieu(ThuongHieu.getTenThuongHieu());
            ThuongHieuUpdate.setTrangThai(ThuongHieu.getTrangThai());
            ThuongHieuUpdate.setUpdatedAt(ThuongHieu.getUpdatedAt());
            return thuongHieuRepository.save(ThuongHieuUpdate);
        } else {
            throw new AppException(ErrorCode.THUONGHIEU_NOT_FOUND);
        }
    }

    @Override
    public String deleteThuongHieu(Long id) {
        getThuongHieuById(id);
        thuongHieuRepository.deleteById(id);
        return "deleted successfull";
    }

}
