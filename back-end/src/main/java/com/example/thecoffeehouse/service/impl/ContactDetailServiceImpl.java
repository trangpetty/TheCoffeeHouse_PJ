package com.example.thecoffeehouse.service.impl;

import com.example.thecoffeehouse.entity.user.ContactDetails;
import com.example.thecoffeehouse.repository.ContactDetailRepository;
import com.example.thecoffeehouse.service.ContactDetailService;
import org.springframework.stereotype.Service;

@Service
public class ContactDetailServiceImpl implements ContactDetailService {

    private final ContactDetailRepository contactDetailRepository;

    public ContactDetailServiceImpl(ContactDetailRepository contactDetailRepository) {
        this.contactDetailRepository = contactDetailRepository;
    }

    @Override
    public ContactDetails getContactDetail(Long id) {
        ContactDetails contactDetails = contactDetailRepository
                                        .findById(id)
                                        .orElseThrow(() -> new RuntimeException("ContactDetail does not exist"));
        return contactDetails;
    }
}
