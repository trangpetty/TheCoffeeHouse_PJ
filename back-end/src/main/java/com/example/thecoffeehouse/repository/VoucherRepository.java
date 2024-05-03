package com.example.thecoffeehouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;

import com.example.thecoffeehouse.entity.Voucher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long>{
    @Query("select v from Voucher v " +
            "where (:name is null or lower(v.name) like %:name%) and (:status is null or v.status = :status) " +
            "and (:applyFrom is null or :applyTo is null or v.createTime between :applyFrom and :applyTo) order by v.id desc")
    Page<Voucher> getAllVouchers(@Param("name") String name, @Param("status") int status, @Param("applyFrom") LocalDateTime applyFrom, @Param("applyTo") LocalDateTime applyTo, Pageable pageable);
}
