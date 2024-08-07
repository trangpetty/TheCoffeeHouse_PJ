package com.example.thecoffeehouse.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_voucher")
public class UserVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userID;

    @Column(name = "voucher_id")
    private Long voucherID;

    @CreationTimestamp
    @Column(name = "issue_date", nullable = false, updatable = false)
    private LocalDateTime createTime;
}
