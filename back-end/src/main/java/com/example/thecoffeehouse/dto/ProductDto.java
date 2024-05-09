package com.example.thecoffeehouse.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private Long TypeID;
    private String name;
    private MultipartFile image;
    private String imageUrl;
    private String size;
    private double price;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}
