package com.example.thecoffeehouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;

import com.example.thecoffeehouse.entity.Voucher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long>{
    @Query("select v from Voucher v " +
            "where (:name is null or lower(v.name) like %:name%) and (:status is null or v.status = :status) " +
            "and (v.applyFrom is null or (:from is null or v.createTime >= :from) and (:to is null or v.createTime <= :to)) order by v.id desc")
    Page<Voucher> getAllVouchers(@Param("name") String name, @Param("status") int status, @Param("applyFrom") Date from, @Param("applyTo") Date to, Pageable pageable);
}
