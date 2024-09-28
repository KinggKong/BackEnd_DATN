package com.example.be_datn.Service;

import com.example.be_datn.Entity.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISizeService {
    List<Size> getAllSize();

    Page<Size> getAllSizePageable(String tenSize, Pageable pageable);

    Size createSize(Size size);

    Size getSizeById(Long id);

    Size updateSize(Long idSize, Size size);

    String deleteSize(Long id);
}
