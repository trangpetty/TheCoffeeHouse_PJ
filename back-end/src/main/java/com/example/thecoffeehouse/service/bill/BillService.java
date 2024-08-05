package com.example.thecoffeehouse.service.bill;

import com.example.thecoffeehouse.dto.bill.BillDto;
import com.example.thecoffeehouse.dto.bill.RevenueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BillService {
    Page<BillDto> getAllBills(String code, String status, Date from, Date to, Pageable pageable);

    BillDto createBill(BillDto billDto);

    BillDto updateBill(String code, BillDto billDto);

    BillDto payWithCash(Long billID, String cashTransactionId);

    BillDto markAsDelivered(Long orderId);

    List<RevenueDTO> getRevenueByType(String type, Integer month, Integer week);

//    List<RevenueDTO> getDailyRevenueForWeekInMonth(int month, int week);

//    List<MonthlyDataDTO> getRevenueByMonth(int year);

    BillDto getBillByCode(String code);

    void updatePaymentStatus(String code, int status);

    void updateDeliveryStatus(String code, String deliveryStatus);

    List<BillDto> getBillsByUserId(Long userID);

    Map<String, Object> getTodayStatistics();

    Double totalValueByUserIDForCurrentYear(Long userID);

    boolean cancelOrder(String code);

    List<Map<String, Object>> getOrders(String reportType, int year, Integer period);

    List<Map<String, Object>> getOrdersByMonthAndWeek(int month, int week);

    List<Map<String, Object>> getOrdersByDate(LocalDate date);
}
