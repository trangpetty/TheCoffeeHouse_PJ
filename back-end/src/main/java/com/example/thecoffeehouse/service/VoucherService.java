package com.example.thecoffeehouse.service;

import com.example.thecoffeehouse.dto.ProductDto;
import com.example.thecoffeehouse.dto.VoucherDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface VoucherService {
    VoucherDto createVoucher(VoucherDto voucherDto);

    void deleteVoucher(Long id);

    Page<VoucherDto> getAllVouchers(String name, int status, Date from, Date to, Pageable pageable);

    VoucherDto updateVoucher(Long id, VoucherDto voucherDto);
}
