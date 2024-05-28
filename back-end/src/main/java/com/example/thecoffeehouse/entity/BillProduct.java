package com.example.thecoffeehouse.entity;

import java.sql.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bill_product")
public class BillProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bill_id")
    private Long billID;
    @Column(name = "product_id")
    private Long productID;
    @Column(name = "product_size_id")
    private String productSizeID;
    @Column(name = "topping_id")
    private String toppingID;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "cost")
    private double cost;
}
