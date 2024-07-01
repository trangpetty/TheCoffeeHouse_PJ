package com.example.thecoffeehouse.controller.bill;

import com.example.thecoffeehouse.dto.MonthlyDataDTO;
import com.example.thecoffeehouse.entity.bill.BillProduct;
import com.example.thecoffeehouse.service.bill.BillProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillProductController {
    private final BillProductService billProductService;


    public BillProductController(BillProductService billProductService) {
        this.billProductService = billProductService;
    }

    @PostMapping("/products")
    public ResponseEntity<BillProduct> createBillProduct(@RequestBody BillProduct billProduct) {
        return new ResponseEntity<>(billProductService.createBillProduct(billProduct), HttpStatus.CREATED);
    }

    @GetMapping("/sale/monthly")
    public ResponseEntity<List<MonthlyDataDTO>> getMonthlySale(@RequestParam int year) {
        List<MonthlyDataDTO> monthlyRevenue = billProductService.getSaleByMonth(year);
        return ResponseEntity.ok(monthlyRevenue);
    }
}
