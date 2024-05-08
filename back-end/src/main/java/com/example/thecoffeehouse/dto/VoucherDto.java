package com.example.thecoffeehouse.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherDto {
    private String id;
    private String title;
    private String image;
    private String code;
    private String description;
    private double value;
    private int status;
    private LocalDateTime applyFrom;
    private LocalDateTime applyTo;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}
