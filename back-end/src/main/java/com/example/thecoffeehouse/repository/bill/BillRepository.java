package com.example.thecoffeehouse.repository.bill;

import com.example.thecoffeehouse.dto.MonthlyDataDTO;
import com.example.thecoffeehouse.dto.bill.RevenueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.bill.Bill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
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

    @Query("SELECT new com.example.thecoffeehouse.dto.bill.RevenueDTO(DAYNAME(b.createTime), COALESCE(SUM(b.value), 0)) " +
            "FROM Bill b " +
            "WHERE b.status = 'success'" +
            "AND MONTH(b.createTime) = :month " +
            "AND FLOOR((DAY(b.createTime) - 1) / 7) + 1 = :week " + // Tuần trong tháng
            "GROUP BY DAYOFWEEK(b.createTime) " +
            "ORDER BY DAYOFWEEK(b.createTime)")
    List<RevenueDTO> findDailyRevenueForWeekInMonth(@Param("month") int month, @Param("week") int week);


    @Query("SELECT new com.example.thecoffeehouse.dto.bill.RevenueDTO(CONCAT('Month ', MONTH(b.createTime)), SUM(b.value)) " +
            "FROM Bill b " +
            "WHERE b.status = 'success'" +
            "GROUP BY MONTH(b.createTime)")
    List<RevenueDTO> findMonthlyRevenue();

    @Query("SELECT new com.example.thecoffeehouse.dto.bill.RevenueDTO(CONCAT('Week ', WEEK(b.createTime)), SUM(b.value)) " +
            "FROM Bill b " +
            "WHERE b.status = 'success'" +
            "GROUP BY WEEK(b.createTime)")
    List<RevenueDTO> findWeeklyRevenue();

    @Query("SELECT new com.example.thecoffeehouse.dto.bill.RevenueDTO(CONCAT('Year ', YEAR(b.createTime)), SUM(b.value)) " +
            "FROM Bill b " +
            "WHERE b.status = 'success'" +
            "GROUP BY YEAR(b.createTime)")
    List<RevenueDTO> findYearlyRevenue();

    @Query("SELECT SUM(b.value) FROM Bill b WHERE b.status = 'success' AND b.createTime BETWEEN :startOfDay AND :endOfDay")
    BigDecimal calculateTotalRevenue(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT COUNT(b) FROM Bill b WHERE b.createTime BETWEEN :startOfDay AND :endOfDay")
    Integer countTotalOrders(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);


//    @Query("SELECT new com.example.thecoffeehouse.dto.bill.RevenueDTO(p.type, SUM(b.createTime)) FROM Bill b JOIN b.products p GROUP BY p.type")
//    List<RevenueDTO> findRevenueByProductType();
}
