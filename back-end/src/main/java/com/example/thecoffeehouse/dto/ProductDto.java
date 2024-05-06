package com.example.thecoffeehouse.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private Long TypeID;
    private String name;
    private String image;
    private String size;
    private double price;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}
