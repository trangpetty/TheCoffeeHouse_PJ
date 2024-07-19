package com.example.thecoffeehouse.controller;

import com.example.thecoffeehouse.dto.VoucherDto;
import com.example.thecoffeehouse.entity.voucher.VoucherType;
import com.example.thecoffeehouse.service.VoucherService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

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
    public ResponseEntity<Page<VoucherDto>> getAllVouchers(@RequestParam(required = false) String name, @RequestParam(required = false) int status,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date applyFrom,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date applyTo,
                                                           @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        if (pageNo > 0) {
            pageNo = pageNo - 1;
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return ResponseEntity.ok(voucherService.getAllVouchers(name, status, applyFrom, applyTo, pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<List<VoucherDto>> getVouchers(@RequestParam(required = false) Long userId) {
        List<VoucherDto> vouchers;

        if (userId != null) {
            vouchers = voucherService.getVouchers(userId);
        } else {
            vouchers = voucherService.getVouchers();
        }

        return ResponseEntity.ok(vouchers);
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

    @PostMapping("/type")
    public ResponseEntity<VoucherType> addVoucherType(@RequestBody VoucherType voucherType) {
        return new ResponseEntity<>(voucherService.createVoucherType(voucherType), HttpStatus.CREATED);
    }

    @GetMapping("/type")
    public ResponseEntity<List<VoucherType>> getVoucherType() {
        return ResponseEntity.ok(voucherService.getVoucherTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoucherDto> getVoucher(@PathVariable Long id) {
        return ResponseEntity.ok(voucherService.getVoucherById(id));
    }
}
