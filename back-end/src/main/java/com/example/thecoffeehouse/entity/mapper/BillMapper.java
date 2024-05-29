package com.example.thecoffeehouse.entity.mapper;

import com.example.thecoffeehouse.dto.BillDto;
import com.example.thecoffeehouse.dto.BillProductDto;
import com.example.thecoffeehouse.entity.Bill;
import com.example.thecoffeehouse.entity.BillProduct;

import java.util.List;
import java.util.stream.Collectors;

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

    public static BillDto mapToBillDto(Bill bill, List<BillProductDto> billProductDtos) {
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
                bill.getModifyTime(),
                billProductDtos
        );

        return billDto;
    }

    public static List<BillProductDto> mapToBillProductsDto(List<BillProduct> billProducts) {
        return billProducts.stream()
                .map(BillMapper::mapToBillProductDto)
                .collect(Collectors.toList());
    }

    public static BillProductDto mapToBillProductDto(BillProduct billProduct) {
        BillProductDto billProductDto = new BillProductDto();
        billProductDto.setId(billProduct.getId());
        billProductDto.setProductID(billProduct.getProductID());
        billProductDto.setProductSizeID(billProduct.getProductSizeID());
        billProductDto.setToppingID(billProduct.getToppingID());
        billProductDto.setQuantity(billProduct.getQuantity());
        billProductDto.setCost(billProduct.getCost());

        return billProductDto;
    }
}
