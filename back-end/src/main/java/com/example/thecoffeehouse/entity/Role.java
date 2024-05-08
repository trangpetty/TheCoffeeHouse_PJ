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
@Document(collection = "role")
public class Role {
    @Id
    private String id;
    private String name;
    private String description;
    private int removable;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date modifyTime;
}
