package com.example.thecoffeehouse.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private String image;
    private String description;
    private double price;
    private List<ProductDetailDto> productSizes;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}
