package com.example.thecoffeehouse.entity.bill;

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

    @Column(name = "user_id")
    private Long userID;
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
    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "payment_status")
    private int paymentStatus;
    @Column(name = "address")
    private String address;
    @Column(name = "delivery_status")
    private String deliveryStatus;

    @CreationTimestamp
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "modify_time")
    private LocalDateTime modifyTime;
}
