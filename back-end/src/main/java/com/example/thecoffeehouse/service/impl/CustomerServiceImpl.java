package com.example.thecoffeehouse.service.impl;

import com.example.thecoffeehouse.dto.CustomerDto;
import com.example.thecoffeehouse.entity.user.*;
import com.example.thecoffeehouse.entity.mapper.CustomerMapper;
import com.example.thecoffeehouse.repository.ContactDetailRepository;
import com.example.thecoffeehouse.repository.CustomerRepository;
import com.example.thecoffeehouse.repository.UserRepository;
import com.example.thecoffeehouse.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final ContactDetailRepository contactDetailRepository;

    public CustomerServiceImpl (CustomerRepository customerRepository, UserRepository userRepository, ContactDetailRepository contactDetailRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.contactDetailRepository = contactDetailRepository;
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
        ContactDetails contactDetails = contactDetailRepository.findFirstByPhoneNumber(phoneNumber);
        if (contactDetails != null) {
            if (contactDetails.getOwnerType() == OwnerType.USER) {
                User user = userRepository.findById(contactDetails.getOwnerID())
                        .orElseThrow(() -> new RuntimeException("User does not exist with phone number: " + phoneNumber));
                return user.getPoint();
            } else if (contactDetails.getOwnerType() == OwnerType.CUSTOMER) {
                Customer customer = customerRepository.findByPhoneNumber(phoneNumber);
                if (customer != null) {
                    return customer.getPoint();
                } else {
                    throw new RuntimeException("Customer does not exist with phone number: " + phoneNumber);
                }
            } else {
                throw new RuntimeException("Unknown owner type for phone number: " + phoneNumber);
            }
        }
        return 0;
    }



    @Override
    public void updateMemberLevel(Customer customer) {
        int points = customer.getPoint();
        MembershipLevel newLevel = MembershipLevel.getLevel(points);
        customer.setMembershipLevel(newLevel.getName());
        customerRepository.save(customer);
    }

}
