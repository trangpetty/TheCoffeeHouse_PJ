package com.example.thecoffeehouse.service.bill;

import com.example.thecoffeehouse.dto.bill.BillDto;
import com.example.thecoffeehouse.dto.MonthlyDataDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface BillService {
    Page<BillDto> getAllBills(String code, String status, Date from, Date to, Pageable pageable);

    BillDto createBill(BillDto billDto);

    BillDto updateBill(String code, BillDto billDto);

    BillDto payWithCash(Long billID, String cashTransactionId);

    BillDto markAsDelivered(Long orderId);

    List<Double> getRevenue();

    List<MonthlyDataDTO> getRevenueByMonth(int year);

    BillDto getBillByCode(String code);

    void updatePaymentStatus(String code, int status);
}
