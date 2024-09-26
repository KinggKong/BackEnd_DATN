package com.example.be_datn.Service;

import com.example.be_datn.Entity.ThuongHieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IThuongHieuService {
    List<ThuongHieu> getAllThuongHieu();

    Page<ThuongHieu> getAllThuongHieuPageable(String tenThuongHieu,Pageable pageable);

    ThuongHieu createThuongHieu(ThuongHieu ThuongHieu);

    ThuongHieu getThuongHieuById(Long id);

    ThuongHieu updateThuongHieu(Long idThuongHieu, ThuongHieu ThuongHieu);

    String deleteThuongHieu(Long id);


}
