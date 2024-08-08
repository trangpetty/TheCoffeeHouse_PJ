package com.example.thecoffeehouse.repository.voucher;

import com.example.thecoffeehouse.entity.voucher.VoucherProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherProductRepository extends JpaRepository<VoucherProduct, Long> {
}
