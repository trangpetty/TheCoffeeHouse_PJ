package com.example.thecoffeehouse.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product")
public class Product {
    @Id
    private String id;
    private String TypeID;
    private String name;
    private String description;
    private String image;
    private String size;
    private double price;

    @CreatedDate
    @Field("createTime")
    private LocalDateTime createTime;

    @LastModifiedDate
    @Field("modifyTime")
    private LocalDateTime modifyTime;

}
