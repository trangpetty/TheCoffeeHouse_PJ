package com.example.thecoffeehouse.repository.bill;

import com.example.thecoffeehouse.dto.MonthlyDataDTO;
import com.example.thecoffeehouse.dto.product.ProductSalesDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.bill.BillProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BillProductRepository extends JpaRepository<BillProduct, Long>{
    @Query("select bp from BillProduct bp " +
            "where bp.billID = :id")
    List<BillProduct> getBillProductByBillID(@Param("id") Long id);

    @Query("SELECT new com.example.thecoffeehouse.dto.MonthlyDataDTO(SUM(bp.quantityProduct), MONTH(b.createTime)) "
            + "FROM BillProduct bp "
            + "JOIN Bill b ON bp.billID = b.id "
            + "WHERE b.status = 'success' "
            + "AND YEAR(b.createTime) = :year "
            + "GROUP BY MONTH(b.createTime)")
    List<MonthlyDataDTO> getMonthlySales(@Param("year") int year);

    @Query("SELECT new com.example.thecoffeehouse.dto.product.ProductSalesDto(p.name, SUM(bp.quantityProduct)) " +
            "FROM BillProduct bp JOIN Bill b ON bp.billID = b.id JOIN Product p ON bp.productID = p.id " +
            "WHERE b.status = 'success' " +
            "AND b.createTime BETWEEN :startDate AND :endDate " +
            "GROUP BY p.name " +
            "ORDER BY SUM(bp.quantityProduct) DESC")
    List<ProductSalesDto> findTopProductsByPeriod(@Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate,
                                                  Pageable pageable);

    @Query("SELECT SUM(bp.quantityProduct) " +
            "FROM BillProduct bp JOIN Bill b ON bp.billID = b.id JOIN Product p ON bp.productID = p.id " +
            "WHERE b.status = 'success' " +
            "AND b.createTime BETWEEN :startOfDay AND :endOfDay")
    Integer calculateTotalProductsSold(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

}
