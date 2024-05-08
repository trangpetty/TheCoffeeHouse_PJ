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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bill")
public class Bill {
    @Id
    private String id;
    private String customerID;
    private String voucherID;
    private double value;
    private double ValueOfVoucher;
    private double ValueOfCustomerPoint;
    private double TotalValue;
    private String code;
    private int point;
    private int status;
    private String address;
    private String note;

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedDate
    private LocalDateTime modifyTime;
}
