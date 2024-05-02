package com.example.thecoffeehouse.service.impl;

import com.example.thecoffeehouse.dto.VoucherDto;
import com.example.thecoffeehouse.repository.VoucherRepository;
import com.example.thecoffeehouse.service.VoucherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;

    public VoucherServiceImpl(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    @Override
    public VoucherDto createVoucher(VoucherDto voucherDto) {
        return null;
    }

    @Override
    public VoucherDto getVoucherById(Long id) {
        return null;
    }

    @Override
    public void deleteVoucher(Long id) {

    }

    @Override
    public Page<VoucherDto> getAllVouchers(String name, int status, Date from, Date to, Pageable pageable) {
        return null;
    }

    @Override
    public VoucherDto updateVoucher(Long id, VoucherDto voucherDto) {
        return null;
    }
}
