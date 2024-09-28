package com.example.be_datn.Service.impl;

import com.example.be_datn.Entity.Size;
import com.example.be_datn.Exception.AppException;
import com.example.be_datn.Exception.ErrorCode;
import com.example.be_datn.Repository.SizeRepository;
import com.example.be_datn.Service.ISizeService;
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
public class SizeService implements ISizeService {
    SizeRepository sizeRepository;

    @Override
    public List<Size> getAllSize() {
        return sizeRepository.findAll();
    }

    @Override
    public Page<Size> getAllSizePageable(String tenSize, Pageable pageable) {
        return sizeRepository.findSizeByTenSizeLike("%" + tenSize + "%", pageable);
    }

    @Override
    public Size createSize(Size size) {
        if (sizeRepository.existsSizeByTenSize(size.getTenSize())) {
            throw new AppException(ErrorCode.SIZE_ALREADY_EXISTS);
        }
        return sizeRepository.save(size);
    }

    @Override
    public Size getSizeById(Long id) {
        return sizeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_FOUND));
    }

    @Override
    public Size updateSize(Long idSize, Size size) {
        Optional<Size> sizeOptional = sizeRepository.findById(idSize);
        if (sizeOptional.isPresent()) {
            Size sizeUpdate = sizeOptional.get();
            sizeUpdate.setTenSize(size.getTenSize());
            sizeUpdate.setTrangThai(size.getTrangThai());
            sizeUpdate.setUpdated_at(size.getUpdated_at());
            return sizeRepository.save(sizeUpdate);
        } else {
            throw new AppException(ErrorCode.SIZE_NOT_FOUND);
        }
    }

    @Override
    public String deleteSize(Long id) {
        getSizeById(id);
        sizeRepository.deleteById(id);
        return "Deleted successfully";
    }
}
