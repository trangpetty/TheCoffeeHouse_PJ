package com.example.thecoffeehouse.entity.mapper;

import com.example.thecoffeehouse.dto.BillDto;
import com.example.thecoffeehouse.entity.Bill;

public class BillMapper {
    public static Bill mapToBill(BillDto billDto) {
        Bill bill = new Bill(
                billDto.getId(),
                billDto.getCustomerID(),
                billDto.getVoucherID(),
                billDto.getValue(),
                billDto.getValueOfVoucher(),
                billDto.getValueOfCustomerPoint(),
                billDto.getTotalValue(),
                billDto.getCode(),
                billDto.getPoint(),
                billDto.getStatus(),
                billDto.getAddress(),
                billDto.getCreateTime(),
                billDto.getModifyTime()
        );

        return bill;
    }

    public static BillDto mapToBillDto(Bill bill) {
        BillDto billDto = new BillDto(
                bill.getId(),
                bill.getCustomerID(),
                bill.getVoucherID(),
                bill.getValue(),
                bill.getValueOfVoucher(),
                bill.getValueOfCustomerPoint(),
                bill.getTotalValue(),
                bill.getCode(),
                bill.getPoint(),
                bill.getStatus(),
                bill.getAddress(),
                bill.getCreateTime(),
                bill.getModifyTime()
        );

        return billDto;
    }
}
