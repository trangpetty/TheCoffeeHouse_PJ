package com.example.thecoffeehouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.thecoffeehouse.entity.Bill;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface BillRepository extends MongoRepository<Bill, String> {
    @Query("{$and:[" +
            "{ $or: [ { 'code': { $regex: ?0, $options: 'i' } }, { ?0: null } ] }, " +
            "{ $or: [ { 'status': ?1 }, { ?1: null } ] }, " +
            "{ $or: [ { 'createTime': { $gte: ?2, $lte: ?3 } }, { ?2: null }, { ?3: null } ] }" +
            "]}")
    Page<Bill> getAllBills(String code, int status, LocalDateTime applyFrom, LocalDateTime applyTo, Pageable pageable);
}
