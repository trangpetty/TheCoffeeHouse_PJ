package com.example.thecoffeehouse.service;

import com.example.thecoffeehouse.dto.CustomerDto;
import com.example.thecoffeehouse.dto.user.ContactDetailDto;
import com.example.thecoffeehouse.entity.user.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    CustomerDto createCustomer(CustomerDto customerDto);

    CustomerDto updateCustomer(Long id, CustomerDto customerDto);

    Page<CustomerDto> getAllCustomers(String name, String phoneNumber, Pageable pageable);

    CustomerDto getCustomerById(Long id);

    void deleteCustomer(Long id);

    int getPoint(String phoneNumber);

    void updateMemberLevel(Customer customer);

    List<ContactDetailDto> getContactDetailsCustomerById(Long id);
}
