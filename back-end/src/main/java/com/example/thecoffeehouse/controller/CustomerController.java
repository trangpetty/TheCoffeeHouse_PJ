package com.example.thecoffeehouse.controller;

import com.example.thecoffeehouse.dto.CustomerDto;
import com.example.thecoffeehouse.dto.ProductDto;
import com.example.thecoffeehouse.service.CustomerService;
import com.example.thecoffeehouse.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerDto> addCustomer(@RequestBody CustomerDto customerDto) {
        return new ResponseEntity<>(customerService.createCustomer(customerDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable String id) {
        CustomerDto customerDto = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerDto);
    }

    @GetMapping
    public ResponseEntity<Page<CustomerDto>> getAllCustomers(@RequestParam("name") String name, @RequestParam("phoneNumber") String phoneNumber, @RequestParam("pageNo") int pageNo, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(pageNo, size);
        return ResponseEntity.ok(customerService.getAllCustomers(name, phoneNumber, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable("id") String id, @RequestBody CustomerDto updateCustomerDto) {
        CustomerDto customerDto = customerService.updateCustomer(id, updateCustomerDto);
        return ResponseEntity.ok(customerDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok("success");
    }
}
