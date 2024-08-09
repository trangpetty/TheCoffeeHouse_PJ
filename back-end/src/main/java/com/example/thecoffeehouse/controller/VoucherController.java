package com.example.thecoffeehouse.controller;

import com.example.thecoffeehouse.dto.VoucherDto;
import com.example.thecoffeehouse.dto.user.VoucherRequest;
import com.example.thecoffeehouse.entity.voucher.VoucherType;
import com.example.thecoffeehouse.service.VoucherService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
            vouchers = voucherService.getVouchersByUserId(userId);
        } else {
            vouchers = voucherService.getVouchers();
        }

        return ResponseEntity.ok(vouchers);
    }

    @GetMapping("/find-by-code")
    public ResponseEntity<VoucherDto> getVoucherByCode(@RequestParam String code) {
        VoucherDto voucherDto = voucherService.getVoucherByCode(code);
        return ResponseEntity.ok(voucherDto);
    }

    @GetMapping("/find-by-phone-number")
    public ResponseEntity<String> getVoucherByPhoneNumber(@RequestParam Long voucherID, @RequestParam String phoneNumber) {
        if(voucherService.getVoucherByPhoneNumber(voucherID, phoneNumber)){
            return ResponseEntity.ok("fail");
        }
        return ResponseEntity.ok("success");
    }

    @PostMapping
    public ResponseEntity<VoucherDto> addVoucher(@RequestBody VoucherRequest voucherRequest) {
        return new ResponseEntity<>(voucherService.createVoucher(voucherRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VoucherDto> updateVoucher(@PathVariable("id") Long id, @RequestBody VoucherRequest voucherRequest) {
        VoucherDto voucherDto = voucherService.updateVoucher(id, voucherRequest);
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
