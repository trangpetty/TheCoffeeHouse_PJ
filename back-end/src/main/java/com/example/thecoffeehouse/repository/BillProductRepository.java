package com.example.thecoffeehouse.repository;

import com.example.thecoffeehouse.entity.BillProduct;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillProductRepository extends MongoRepository<BillProduct, String> {
    @Query("{ 'billID' : ?0 }")
    List<BillProduct> getBillProductByBillID(String id);
}
