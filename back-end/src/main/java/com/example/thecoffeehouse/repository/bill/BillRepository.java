package com.example.thecoffeehouse.repository.bill;

import com.example.thecoffeehouse.dto.MonthlyDataDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.bill.Bill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long>{
    @Query("select b from Bill b " +
            "where (:code is null or lower(b.code) like %:code%) and (:status is null or lower(b.status) like %:status%) " +
            "and (:applyFrom IS NULL or :applyTo IS NULL or b.createTime between :applyFrom and :applyTo) order by b.id desc")
    Page<Bill> getAllBills(@Param("code") String code, @Param("status") String status, @Param("applyFrom") LocalDateTime applyFrom, @Param("applyTo") LocalDateTime applyTo, Pageable pageable);

    @Query("select b from Bill b where (lower(b.code) like %:code%)")
    Bill getBillByCode(@Param("code") String code);

    @Query("SELECT SUM(b.value) " +
            "FROM Bill b " +
            "WHERE FUNCTION('MONTH', b.createTime) = :month ")
    Double findRevenueByMonth(@Param("month") int month);

    @Query("SELECT new com.example.thecoffeehouse.dto.MonthlyDataDTO(SUM(b.value), MONTH(b.createTime)) "
            + "FROM Bill b "
            + "WHERE YEAR(b.createTime) = :year "
            + "GROUP BY MONTH(b.createTime)")
    List<MonthlyDataDTO> getMonthlyRevenue(@Param("year") int year);

    List<Bill> findByUserID(Long userID);

    boolean existsByUserIDAndVoucherID(Long userID, Long voucherID);

    Bill findByCode(String code);
}
