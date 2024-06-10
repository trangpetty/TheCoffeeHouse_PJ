package com.example.thecoffeehouse.entity.voucher;

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
@Table(name = "voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private String code;
    @Column(name = "voucher_type_id")
    private Long voucherTypeID;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "image")
    private String image;
    @Column(name = "discount_value", columnDefinition = "DOUBLE DEFAULT 0")
    private double discountValue;
    @Column(name = "minimum_order_value", columnDefinition = "DOUBLE DEFAULT 0")
    private double minimumOrderValue;
    @Column(name = "max_uses", columnDefinition = "INT DEFAULT 0")
    private int maxUses;
    @Column(name = "minimum_items", columnDefinition = "INT DEFAULT 0")
    private int minimumItems;
    @Column(name = "current_uses", columnDefinition = "INT DEFAULT 0")
    private int currentUses;
    @Column(name = "status", columnDefinition = "INT DEFAULT 0")
    private int status;
    @Column(name = "error_message")
    private String errorMessage;
    @Column(name = "apply_from", nullable = false)
    private LocalDateTime applyFrom;
    @Column(name = "apply_to", nullable = false)
    private LocalDateTime applyTo;

    @CreationTimestamp
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "modify_time")
    private LocalDateTime modifyTime;
}
