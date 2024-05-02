package com.example.thecoffeehouse.entity.mapper;

import com.example.thecoffeehouse.dto.CustomerDto;
import com.example.thecoffeehouse.entity.Customer;

public class CustomerMapper {
    public static Customer mapToCustomer(CustomerDto customerDto) {
        Customer customer = new Customer(
                customerDto.getId(),
                customerDto.getName(),
                customerDto.getAddress(),
                customerDto.getPhoneNumber(),
                customerDto.getPoint(),
                customerDto.getCreateTime(),
                customerDto.getModifyTime()
        );

        return customer;
    }

    public static CustomerDto mapToCustomerDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto(
                customer.getId(),
                customer.getName(),
                customer.getAddress(),
                customer.getPhoneNumber(),
                customer.getPoint(),
                customer.getCreateTime(),
                customer.getModifyTime()
        );

        return customerDto;
    }
}
