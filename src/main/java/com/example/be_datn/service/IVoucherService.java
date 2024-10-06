package com.example.be_datn.service;

import com.example.be_datn.dto.Request.VoucherRequest;
import com.example.be_datn.dto.Response.VoucherResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IVoucherService {

    Page<VoucherResponse> getAllVoucherPageable(String tenChienDich, Pageable pageable);

    VoucherResponse createVoucher(VoucherRequest voucherRequest);

    VoucherResponse getVoucherById(Long id);

    VoucherResponse updateVoucher(Long idVoucher, VoucherRequest voucherRequest);

    String deleteVoucher(Long id);
}
