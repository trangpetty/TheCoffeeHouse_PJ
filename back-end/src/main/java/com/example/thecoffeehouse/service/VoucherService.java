package com.example.thecoffeehouse.service;

import com.example.thecoffeehouse.dto.VoucherDto;
import com.example.thecoffeehouse.dto.user.VoucherRequest;
import com.example.thecoffeehouse.entity.voucher.VoucherType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface VoucherService {
    VoucherDto createVoucher(VoucherRequest voucherRequest);

    void deleteVoucher(Long id);

    Page<VoucherDto> getAllVouchers(String name, int status, Date from, Date to, Pageable pageable);

    VoucherDto updateVoucher(Long id, VoucherDto voucherDto);

    List<VoucherDto> getVouchers();

    List<VoucherDto> getVouchersByUserId(Long userID);

    VoucherType createVoucherType(VoucherType voucherType);

    List<VoucherType> getVoucherTypes();

    VoucherDto getVoucherById(Long id);

    VoucherDto getVoucherByCode(String code);

    Boolean getVoucherByPhoneNumber(Long voucherID, String phoneNumber);

    void assignVouchersToUser(Long userId);
}
