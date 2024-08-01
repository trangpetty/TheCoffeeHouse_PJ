package com.example.thecoffeehouse.service.impl;

import com.example.thecoffeehouse.dto.CustomerDto;
import com.example.thecoffeehouse.entity.user.Customer;
import com.example.thecoffeehouse.entity.mapper.CustomerMapper;
import com.example.thecoffeehouse.entity.user.MembershipLevel;
import com.example.thecoffeehouse.repository.CustomerRepository;
import com.example.thecoffeehouse.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl (CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerMapper.mapToCustomerDto(savedCustomer);
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Customer customer = customerRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Customer does not exists"));
        customer.setDefaultName(customerDto.getName());
        customer.setDefaultAddress(customerDto.getAddress());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setPoint(customerDto.getPoint());
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerMapper.mapToCustomerDto(savedCustomer);
    }

    @Override
    public Page<CustomerDto> getAllCustomers(String name, String phoneNumber, Pageable pageable) {
        Page<Customer> customers = customerRepository.getAllCustomers(name, phoneNumber, pageable);
        return customers.map(CustomerMapper::mapToCustomerDto);
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Customer does not exists"));
        return CustomerMapper.mapToCustomerDto(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Customer does not exists"));
        customerRepository.deleteById(id);
    }

    @Override
    public int getPoint(String phoneNumber) {
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber);
        return customer.getPoint();
    }

    @Override
    public void updateMemberLevel(Customer customer) {
        int points = customer.getPoint();
        MembershipLevel newLevel = MembershipLevel.getLevel(points);
        customer.setMembershipLevel(newLevel.getName());
        customerRepository.save(customer);
    }

}
