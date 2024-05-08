package com.example.thecoffeehouse.entity;

import java.sql.Date;
import java.time.LocalDateTime;
;
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
@Document(collection = "voucher")
public class Voucher {
    @Id
    private String id;
    private String title;
    private String image;
    private String code;
    private String description;
    private double value;
    private int status;
    private LocalDateTime applyFrom;
    private LocalDateTime applyTo;

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedDate
    private LocalDateTime modifyTime;
}
