package com.example.thecoffeehouse.entity;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bill_product")
public class BillProduct {
    @Id
    private String id;
    private String billID;
    private String productID;
    private int quantity;
    private double price;
}
