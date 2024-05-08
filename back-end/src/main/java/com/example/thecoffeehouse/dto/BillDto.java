package com.example.thecoffeehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillDto {
    private String id;
    private String customerID;
    private String voucherID;
    private double value;
    private double ValueOfVoucher;
    private double ValueOfCustomerPoint;
    private double TotalValue;
    private String code;
    private int point;
    private int status;
    private String address;
    private String note;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}
