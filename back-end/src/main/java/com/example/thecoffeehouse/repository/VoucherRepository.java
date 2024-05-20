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
    @Query("SELECT v FROM Voucher v " +
            "WHERE (:name IS NULL OR LOWER(v.name) LIKE %:name%) " +
            "AND (:status IS NULL OR v.status = :status) " +
            "AND (:applyFrom IS NULL OR v.applyFrom >= :applyFrom) " +
            "AND (:applyTo IS NULL OR v.applyTo <= :applyTo) " +
            "ORDER BY v.id DESC")
    Page<Voucher> getAllVouchers(@Param("name") String name, @Param("status") int status, @Param("applyFrom") LocalDateTime applyFrom, @Param("applyTo") LocalDateTime applyTo, Pageable pageable);
}
