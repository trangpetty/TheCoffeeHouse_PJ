package com.example.thecoffeehouse.repository.voucher;

import com.example.thecoffeehouse.entity.voucher.VoucherType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherTypeRepository extends JpaRepository<VoucherType, Long> {
    VoucherType findByType(String type);
}
