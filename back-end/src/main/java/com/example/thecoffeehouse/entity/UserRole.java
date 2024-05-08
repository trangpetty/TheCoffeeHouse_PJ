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
@Document(collection = "user_role")
public class UserRole {
    @Id
    private String id;
    private String userID;
    private String roleID;
    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date modifyTime;
}
