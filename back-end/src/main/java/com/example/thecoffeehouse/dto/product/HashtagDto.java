package com.example.thecoffeehouse.dto.product;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
public class HashtagDto {
    private Long id;
    private String name;
    private List<Long> products;
}
