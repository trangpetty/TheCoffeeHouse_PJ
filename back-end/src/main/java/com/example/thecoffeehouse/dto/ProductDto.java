package com.example.thecoffeehouse.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String id;
    private String TypeID;
    private String name;
    private String description;
    private String image;
    private String size;
    private double price;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}
