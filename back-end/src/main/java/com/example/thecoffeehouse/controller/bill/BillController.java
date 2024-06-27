package com.example.thecoffeehouse.controller.bill;

import com.example.thecoffeehouse.dto.bill.BillDto;
import com.example.thecoffeehouse.dto.MonthlyDataDTO;
import com.example.thecoffeehouse.service.bill.BillService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {
    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping
    public ResponseEntity<Page<BillDto>> getAllBills(@RequestParam("code") String code, @RequestParam("status") int status,
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

    @PutMapping("/{id}")
    public ResponseEntity<BillDto> updateBill(@PathVariable("id") long id, @RequestBody BillDto updateBillDto) {
        BillDto billDto = billService.updateBill(id, updateBillDto);
        return ResponseEntity.ok(billDto);
    }

    @GetMapping("/revenue")
    public List<Double> getRevenue() {
        return billService.getRevenue();
    }

    @GetMapping("/revenue/monthly")
    public ResponseEntity<List<MonthlyDataDTO>> getMonthlyRevenue(@RequestParam int year) {
        List<MonthlyDataDTO> monthlyRevenue = billService.getRevenueByMonth(year);
        return ResponseEntity.ok(monthlyRevenue);
    }
}
