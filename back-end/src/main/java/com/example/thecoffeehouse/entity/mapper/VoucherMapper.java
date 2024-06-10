package com.example.thecoffeehouse.entity.mapper;

import com.example.thecoffeehouse.dto.VoucherDto;
import com.example.thecoffeehouse.entity.voucher.Voucher;
import com.example.thecoffeehouse.entity.voucher.VoucherType;

public class VoucherMapper {
    public static Voucher mapToVoucher(VoucherDto voucherDto) {
        Voucher voucher = new Voucher(
        );
        voucher.setId(voucherDto.getId());
        voucher.setName(voucherDto.getName());
        voucher.setCode(voucherDto.getCode());
        voucher.setVoucherTypeID(voucherDto.getVoucherTypeID());
        voucher.setDescription(voucherDto.getDescription());
        voucher.setImage(voucherDto.getImage());
        voucher.setDiscountValue(voucherDto.getDiscountValue());
        voucher.setMinimumOrderValue(voucherDto.getMinimumOrderValue());
        voucher.setMaxUses(voucherDto.getMaxUses());
        voucher.setMinimumItems(voucherDto.getMinimumItems());
        voucher.setCurrentUses(voucherDto.getCurrentUses());
        voucher.setStatus(voucherDto.getStatus());
        voucher.setErrorMessage(voucherDto.getErrorMessage());
        voucher.setApplyFrom(voucherDto.getApplyFrom());
        voucher.setApplyTo(voucherDto.getApplyTo());
        voucher.setCreateTime(voucherDto.getCreateTime());
        voucher.setModifyTime(voucherDto.getModifyTime());

        return voucher;
    }

    public static VoucherDto mapToVoucherDto(Voucher voucher, VoucherType voucherType) {
        VoucherDto voucherDto = new VoucherDto();
        voucherDto.setId(voucher.getId());
        voucherDto.setName(voucher.getName());
        voucherDto.setCode(voucher.getCode());
        voucherDto.setVoucherTypeID(voucher.getVoucherTypeID());
        voucherDto.setVoucherType(voucherType.getType());
        voucherDto.setDescription(voucher.getDescription());
        voucherDto.setImage(voucher.getImage());
        voucherDto.setDiscountValue(voucher.getDiscountValue());
        voucherDto.setMinimumOrderValue(voucher.getMinimumOrderValue());
        voucherDto.setMinimumItems(voucher.getMinimumItems());
        voucherDto.setMaxUses(voucher.getMaxUses());
        voucherDto.setCurrentUses(voucher.getCurrentUses());
        voucherDto.setStatus(voucher.getStatus());
        voucherDto.setErrorMessage(voucher.getErrorMessage());
        voucherDto.setApplyFrom(voucher.getApplyFrom());
        voucherDto.setApplyTo(voucher.getApplyTo());
        voucherDto.setCreateTime(voucher.getCreateTime());
        voucherDto.setModifyTime(voucher.getModifyTime());


        return voucherDto;
    }

}
