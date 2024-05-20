package com.example.thecoffeehouse.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String description;
    private String image;
    private double value;
    private int status;
    private LocalDateTime applyFrom;
    private LocalDateTime applyTo;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}
