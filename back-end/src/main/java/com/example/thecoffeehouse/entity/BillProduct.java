package com.example.thecoffeehouse.entity;

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
    private Long productSizeID;
    @Column(name = "topping_id")
    private Long toppingID;
    @Column(name = "quantity_product", nullable = false)
    private int quantityProduct = 0;
    @Column(name = "quantity_topping", nullable = false)
    private int quantityTopping = 0;
    @Column(name = "cost", nullable = false)
    private double cost = 0.0;
}
