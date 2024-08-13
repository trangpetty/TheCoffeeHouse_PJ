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

    @Column(name = "combo_price", columnDefinition = "DOUBLE DEFAULT 0")
    private double comboPrice; // Giá combo (dùng cho voucher combo)

    @Column(name = "fixed_price", columnDefinition = "DOUBLE DEFAULT 0")
    private double fixedPrice; // Giá cố định cho sản phẩm (dùng cho đồng giá)

    @Column(name = "product_type")
    private String productType; // Loại sản phẩm áp dụng voucher (dùng cho đồng giá sản phẩm loại đá xay)

    @Column(name = "free_ship", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean freeShip; // miễn phí vận chuyển

    @Column(name = "buy_1_get_1", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean buy1Get1; // mua 1 tặng 1

    @Column(name = "combo_quantity")
    private Integer comboQuantity; // Số lượng tối thiểu để áp dụng combo

    @Column(name = "discount_max")
    private Double discountMax;

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
