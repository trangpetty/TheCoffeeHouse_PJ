package com.example.thecoffeehouse.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherDto {
    private Long id;
    private String name;
    private String code;
    private Long voucherTypeID;
    private String voucherType;
    private String description;
    private String image;
    private double discountValue;
    private double minimumOrderValue;
    private int minimumItems;
    private int maxUses;
    private int currentUses;
    private int status;
    private String errorMessage;
    private LocalDateTime applyFrom;
    private LocalDateTime applyTo;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}
