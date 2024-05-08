package com.example.thecoffeehouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import com.example.thecoffeehouse.entity.Voucher;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends MongoRepository<Voucher, String> {
    @Query("{ " +
            "$or: [ { 'code': { $regex: ?0, $options: 'i' } }, { 'code': null } ], " +
            "'status': { $eq: ?1 }, " +
            "'createTime': { $gte: ?2, $lte: ?3 } " +
            "}")
    Page<Voucher> getAllVouchers(String code, int status, LocalDateTime applyFrom, LocalDateTime applyTo, Pageable pageable);
}
