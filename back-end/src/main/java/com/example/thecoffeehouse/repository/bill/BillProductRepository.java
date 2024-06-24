package com.example.thecoffeehouse.repository.bill;

import com.example.thecoffeehouse.dto.MonthlyDataDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.bill.BillProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillProductRepository extends JpaRepository<BillProduct, Long>{
    @Query("select bp from BillProduct bp " +
            "where bp.billID = :id")
    List<BillProduct> getBillProductByBillID(@Param("id") Long id);

    @Query("SELECT new com.example.thecoffeehouse.dto.MonthlyDataDTO(SUM(bp.quantityProduct), MONTH(b.createTime)) "
            + "FROM BillProduct bp "
            + "JOIN Bill b ON bp.billID = b.id "
            + "WHERE YEAR(b.createTime) = :year "
            + "GROUP BY MONTH(b.createTime)")
    List<MonthlyDataDTO> getMonthlySales(@Param("year") int year);
}
