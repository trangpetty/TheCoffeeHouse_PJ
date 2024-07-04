package com.example.thecoffeehouse.controller;

import com.example.thecoffeehouse.dto.VoucherDto;
import com.example.thecoffeehouse.entity.ProductType;
import com.example.thecoffeehouse.service.VoucherService;
import com.example.thecoffeehouse.service.product.ProductService;
import com.example.thecoffeehouse.service.product.ProductTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final VoucherService voucherService;
    private final ProductTypeService productTypeService;
    private final ProductService productService;

    public OrderController(VoucherService voucherService, ProductService productService, ProductTypeService productTypeService) {
        this.voucherService = voucherService;
        this.productTypeService = productTypeService;
        this.productService = productService;
    }

    @GetMapping("/vouchers/all")
    public ResponseEntity<List<VoucherDto>> getVouchers() {
        return ResponseEntity.ok(voucherService.getVouchers());
    }

    @GetMapping("/product-types")
    public ResponseEntity<List<ProductType>> getAllProductTypes() {
        List<ProductType> productTypes = productTypeService.findAll();
        return ResponseEntity.ok(productTypes);
    }

}
