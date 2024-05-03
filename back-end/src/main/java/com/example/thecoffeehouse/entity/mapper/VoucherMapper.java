package com.example.thecoffeehouse.entity.mapper;

import com.example.thecoffeehouse.dto.VoucherDto;
import com.example.thecoffeehouse.entity.Voucher;

public class VoucherMapper {
    public static Voucher mapToVoucher(VoucherDto voucherDto) {
        Voucher voucher = new Voucher(
                voucherDto.getId(),
                voucherDto.getName(),
                voucherDto.getImage(),
                voucherDto.getValue(),
                voucherDto.getStatus(),
                voucherDto.getApplyFrom(),
                voucherDto.getApplyTo(),
                voucherDto.getCreateTime(),
                voucherDto.getModifyTime()
        );

        return voucher;
    }

    public static VoucherDto mapToVoucherDto(Voucher voucher) {
        VoucherDto voucherDto = new VoucherDto(
                voucher.getId(),
                voucher.getName(),
                voucher.getImage(),
                voucher.getValue(),
                voucher.getStatus(),
                voucher.getApplyFrom(),
                voucher.getApplyTo(),
                voucher.getCreateTime(),
                voucher.getModifyTime()
        );

        return voucherDto;
    }
}
