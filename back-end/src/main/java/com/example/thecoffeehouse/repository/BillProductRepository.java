package com.example.thecoffeehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.BillProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillProductRepository extends JpaRepository<BillProduct, Long>{
    @Query("select bp from BillProduct bp " +
            "where bp.billID = :id")
    List<BillProduct> getBillProductByBillID(@Param("id") Long id);
}
