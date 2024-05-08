package com.example.thecoffeehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private String id;
    private String name;
    private String address;
    private String phoneNumber;
    private int point;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}
