package com.example.thecoffeehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillDto {
    private Long id;
    private Long userID;
    private Long voucherID;
    private double value;
    private double ValueOfVoucher;
    private double ValueOfCustomerPoint;
    private double TotalValue;
    private String code;
    private String address;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
    private List<BillProductDto> products;
    private String paymentMethod;
    private int paymentStatus;
    private String deliveryStatus;
}
