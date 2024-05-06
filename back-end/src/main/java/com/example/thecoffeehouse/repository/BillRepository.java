package com.example.thecoffeehouse.repository;

import com.example.thecoffeehouse.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.Bill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface BillRepository extends JpaRepository<Bill, Long>{
    @Query("select b from Bill b " +
            "where (:code is null or lower(b.code) like %:code%) and (:status is null or b.status = :status) " +
            "and (:applyFrom is null or :applyTo is null or b.createTime between :applyFrom and :applyTo) order by b.id desc")
    Page<Bill> getAllBills(@Param("code") String code, @Param("status") int status, @Param("applyFrom") LocalDateTime applyFrom, @Param("applyTo") LocalDateTime applyTo, Pageable pageable);
}
