package com.example.thecoffeehouse.service;

import com.example.thecoffeehouse.dto.CustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    CustomerDto createCustomer(CustomerDto customerDto);

    CustomerDto updateCustomer(String id, CustomerDto customerDto);

    Page<CustomerDto> getAllCustomers(String name, String phoneNumber, Pageable pageable);

    CustomerDto getCustomerById(String id);

    void deleteCustomer(String id);
}
