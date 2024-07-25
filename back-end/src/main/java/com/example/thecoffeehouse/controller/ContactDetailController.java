package com.example.thecoffeehouse.controller;

import com.example.thecoffeehouse.entity.user.ContactDetails;
import com.example.thecoffeehouse.service.ContactDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact-detail")
public class ContactDetailController {
    private final ContactDetailService contactDetailService;

    public ContactDetailController(ContactDetailService contactDetailService) {
        this.contactDetailService = contactDetailService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDetails> getContactDetail(@PathVariable Long id) {
        return ResponseEntity.ok(contactDetailService.getContactDetail(id));
    }
}
