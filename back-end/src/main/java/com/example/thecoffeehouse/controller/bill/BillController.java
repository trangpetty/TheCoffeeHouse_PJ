package com.example.thecoffeehouse.controller.bill;

import com.example.thecoffeehouse.dto.OrderStatus;
import com.example.thecoffeehouse.dto.bill.BillDto;
import com.example.thecoffeehouse.dto.MonthlyDataDTO;
import com.example.thecoffeehouse.dto.bill.RevenueDTO;
import com.example.thecoffeehouse.service.bill.BillService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bills")
public class BillController {
    private final BillService billService;
    private final SimpMessagingTemplate messagingTemplate;

    public BillController(BillService billService, SimpMessagingTemplate messagingTemplate) {
        this.billService = billService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping
    public ResponseEntity<Page<BillDto>> getAllBills(@RequestParam("code") String code, @RequestParam("status") String status,
                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date applyFrom,
                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date applyTo,
                                                     @RequestParam("pageNo") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        if (pageNo > 0) {
            pageNo = pageNo - 1;
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return ResponseEntity.ok(billService.getAllBills(code, status, applyFrom, applyTo, pageable));
    }

    @PostMapping
    public ResponseEntity<BillDto> createBill(@RequestBody BillDto billDto) {
        return new ResponseEntity<>(billService.createBill(billDto), HttpStatus.CREATED);
    }

    @PutMapping("/{code}")
    public ResponseEntity<BillDto> updateBill(@PathVariable("code") String code, @RequestBody BillDto updateBillDto) {
        BillDto billDto = billService.updateBill(code, updateBillDto);
        return ResponseEntity.ok(billDto);
    }

    @PutMapping("/delivery/{code}")
    public ResponseEntity<String> updateDeliveryStatus(@PathVariable("code") String code, @RequestParam String deliveryStatus) {
        try {
            // Cập nhật trạng thái giao hàng
            billService.updateDeliveryStatus(code, deliveryStatus);

            // Tạo đối tượng trạng thái đơn hàng để gửi
            OrderStatus orderStatus = new OrderStatus();
            orderStatus.setCode(code);
            orderStatus.setStatus(deliveryStatus);

            // Gửi thông báo đến các client qua WebSocket
            messagingTemplate.convertAndSend("/topic/statusUpdates", orderStatus);

            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("fail");
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<BillDto> getBill(@PathVariable String code) {
        BillDto billDto = billService.getBillByCode(code);
        return ResponseEntity.ok(billDto);
    }

    @GetMapping("/revenue")
    public ResponseEntity<List<RevenueDTO>> getRevenue(@RequestParam(name = "type") String type,
                                                       @RequestParam(name = "month", required = false) Integer month,
                                                       @RequestParam(name = "week", required = false) Integer week) {
        List<RevenueDTO> revenue = billService.getRevenueByType(type, month, week);
        return new ResponseEntity<>(revenue, HttpStatus.OK);
    }

    @GetMapping("/today-statistics")
    public ResponseEntity<Map<String, Object>> getTodayStatistics() {
        return ResponseEntity.ok(billService.getTodayStatistics());
    }


//    @GetMapping("/revenue/monthly")
//    public ResponseEntity<List<MonthlyDataDTO>> getMonthlyRevenue(@RequestParam int year) {
//        List<MonthlyDataDTO> monthlyRevenue = billService.getRevenueByMonth(year);
//        return ResponseEntity.ok(monthlyRevenue);
//    }
}
