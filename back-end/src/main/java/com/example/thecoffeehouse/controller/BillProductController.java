package com.example.thecoffeehouse.controller;

import com.example.thecoffeehouse.entity.BillProduct;
import com.example.thecoffeehouse.service.BillProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillProductController {
    private final BillProductService billProductService;


    public BillProductController(BillProductService billProductService) {
        this.billProductService = billProductService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<BillProduct>> getBill(@PathVariable Long id) {
        List<BillProduct> billProducts = billProductService.getBillProductByBillID(id);
        return ResponseEntity.ok(billProducts);
    }

    @PostMapping("/products")
    public ResponseEntity<BillProduct> createBillProduct(@RequestBody BillProduct billProduct) {
        return new ResponseEntity<>(billProductService.createBillProduct(billProduct), HttpStatus.CREATED);
    }
}
