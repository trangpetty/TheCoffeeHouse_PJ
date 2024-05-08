package com.example.thecoffeehouse.entity;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "blog_image")
public class BlogImage {
    @Id
    private String id;
    private String blogID;
    private String image;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date modifyTime;
}
