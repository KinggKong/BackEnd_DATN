
package com.example.be_datn.service.impl;

import com.example.be_datn.entity.KichThuoc;
import com.example.be_datn.exception.AppException;
import com.example.be_datn.exception.ErrorCode;
import com.example.be_datn.repository.KichThuocRepository;
import com.example.be_datn.service.IKichThuocService;
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
public class KichThuocService implements IKichThuocService {
    KichThuocRepository kichThuocRepository;

    @Override
    public List<KichThuoc> getAllKichThuoc() {
        return kichThuocRepository.findAll();
    }

    @Override
    public Page<KichThuoc> getAllKichThuocPageable(String tenKichThuoc, Pageable pageable) {
        return kichThuocRepository.findKichThuocByTenKichThuocLike("%" + tenKichThuoc + "%", pageable);
    }

    @Override
    public KichThuoc createKichThuoc(KichThuoc kichThuoc) {
        if (kichThuocRepository.existsKichThuocByTenKichThuoc(kichThuoc.getTenKichThuoc())) {
            throw new AppException(ErrorCode.KICHTHUOC_ALREADY_EXISTS); // Thay đổi mã lỗi nếu cần
        }
        return kichThuocRepository.save(kichThuoc);
    }

    @Override
    public KichThuoc getKichThuocById(Long id) {
        return kichThuocRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.KICHTHUOC_NOT_FOUND)); // Thay đổi mã lỗi nếu cần
    }

    @Override
    public KichThuoc updateKichThuoc(Long idKichThuoc, KichThuoc kichThuoc) {
        Optional<KichThuoc> kichThuocOptional = kichThuocRepository.findById(idKichThuoc);
        if (kichThuocOptional.isPresent()) {
            KichThuoc kichThuocUpdate = kichThuocOptional.get();
            kichThuocUpdate.setTenKichThuoc(kichThuoc.getTenKichThuoc());
            kichThuocUpdate.setTrangThai(kichThuoc.getTrangThai());
            kichThuocUpdate.setUpdatedAt(kichThuoc.getUpdatedAt()); // Đảm bảo rằng tên phương thức chính xác
            return kichThuocRepository.save(kichThuocUpdate);
        } else {
            throw new AppException(ErrorCode.KICHTHUOC_NOT_FOUND); // Thay đổi mã lỗi nếu cần
        }
    }

    @Override
    public String deleteKichThuoc(Long id) {
        getKichThuocById(id);
        kichThuocRepository.deleteById(id);
        return "deleted successfully";
    }
}