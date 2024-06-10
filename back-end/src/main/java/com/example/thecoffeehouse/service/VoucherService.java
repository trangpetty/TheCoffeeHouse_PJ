package com.example.thecoffeehouse.service;

import com.example.thecoffeehouse.dto.VoucherDto;
import com.example.thecoffeehouse.entity.voucher.VoucherType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface VoucherService {
    VoucherDto createVoucher(VoucherDto voucherDto);

    void deleteVoucher(Long id);

    Page<VoucherDto> getAllVouchers(String name, int status, Date from, Date to, Pageable pageable);

    VoucherDto updateVoucher(Long id, VoucherDto voucherDto);

    List<VoucherDto> getVouchers();

    VoucherType createVoucherType(VoucherType voucherType);

    List<VoucherType> getVoucherTypes();
}
