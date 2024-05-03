package com.example.thecoffeehouse.controller;

import com.example.thecoffeehouse.dto.VoucherDto;
import com.example.thecoffeehouse.service.VoucherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherController {
    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @GetMapping
    public ResponseEntity<Page<VoucherDto>> getAllVouchers(@RequestParam("name") String name, @RequestParam(value = "status",  required = false) int status,
                                                           @RequestParam(value = "applyFrom", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date applyFrom,
                                                           @RequestParam(value = "applyTo", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date applyTo,
                                                           @RequestParam("pageNo") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return ResponseEntity.ok(voucherService.getAllVouchers(name, status, applyFrom, applyTo, pageable));
    }

    @PostMapping
    public ResponseEntity<VoucherDto> addVoucher(@RequestBody VoucherDto voucherDto) {
        return new ResponseEntity<>(voucherService.createVoucher(voucherDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VoucherDto> updateVoucher(@PathVariable("id") Long id, @RequestBody VoucherDto updateVoucherDto) {
        VoucherDto voucherDto = voucherService.updateVoucher(id, updateVoucherDto);
        return ResponseEntity.ok(voucherDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVoucher(@PathVariable("id") Long id) {
        voucherService.deleteVoucher(id);
        return ResponseEntity.ok("success");
    }
}
