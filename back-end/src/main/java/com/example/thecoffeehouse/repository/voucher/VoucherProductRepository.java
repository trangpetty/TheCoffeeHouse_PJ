package com.example.thecoffeehouse.repository.voucher;

import com.example.thecoffeehouse.entity.voucher.VoucherProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoucherProductRepository extends JpaRepository<VoucherProduct, Long> {
    List<VoucherProduct> findByVoucherID(Long voucherID);

    void deleteByVoucherID(Long voucherID);
}
