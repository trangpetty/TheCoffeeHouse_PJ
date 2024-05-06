package com.example.thecoffeehouse.entity;

import java.time.LocalDateTime;

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
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerID;
    @Column(name = "voucher_id")
    private Long voucherID;
    @Column(name = "value")
    private double value;
    @Column(name = "value_of_voucher")
    private double ValueOfVoucher;
    @Column(name = "value_of_customer_point")
    private double ValueOfCustomerPoint;
    @Column(name = "total_value")
    private double TotalValue;
    @Column(name = "code")
    private String code;
    @Column(name = "point")
    private int point;
    @Column(name = "status")
    private int status;
    @Column(name = "address")
    private String address;

    @CreationTimestamp
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "modify_time")
    private LocalDateTime modifyTime;
}
