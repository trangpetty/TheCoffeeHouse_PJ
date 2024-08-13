package com.example.thecoffeehouse.controller;

import com.example.thecoffeehouse.dto.CustomerDto;
import com.example.thecoffeehouse.dto.user.ContactDetailDto;
import com.example.thecoffeehouse.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerDto> addCustomer(@RequestBody CustomerDto customerDto) {
        return new ResponseEntity<>(customerService.createCustomer(customerDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        CustomerDto customerDto = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerDto);
    }

    @GetMapping
    public ResponseEntity<Page<CustomerDto>> getAllCustomers(@RequestParam("name") String name, @RequestParam("phoneNumber") String phoneNumber, @RequestParam("pageNo") int pageNo, @RequestParam(defaultValue = "10") int size) {
        if (pageNo > 0) {
            pageNo = pageNo - 1;
        }
        Pageable pageable = PageRequest.of(pageNo, size);
        return ResponseEntity.ok(customerService.getAllCustomers(name, phoneNumber, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable("id") Long id, @RequestBody CustomerDto updateCustomerDto) {
        CustomerDto customerDto = customerService.updateCustomer(id, updateCustomerDto);
        return ResponseEntity.ok(customerDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/get-point")
    public ResponseEntity<Integer> getPoint(@RequestParam("phoneNumber") String phoneNumber) {
        return ResponseEntity.ok(customerService.getPoint(phoneNumber));
    }

    @GetMapping("/detail/{id}")
    public List<ContactDetailDto> getDetailsUserById(@PathVariable Long id) {
        return customerService.getContactDetailsCustomerById(id);
    }
}
