package com.example.thecoffeehouse.entity.mapper;

import com.example.thecoffeehouse.dto.user.ContactDetailDto;
import com.example.thecoffeehouse.entity.user.ContactDetails;

public class ContactDetailMapper {
    public static ContactDetailDto mapToContactDetailDto(ContactDetails contactDetails) {
        ContactDetailDto contactDetailDto = new ContactDetailDto();
        contactDetailDto.setId(contactDetails.getId());
        contactDetailDto.setName(contactDetails.getName());
        contactDetailDto.setPhoneNumber(contactDetails.getPhoneNumber());
        contactDetailDto.setAddress(contactDetails.getAddress());
        contactDetailDto.setOwnerID(contactDetails.getOwnerID());
        contactDetailDto.setOwnerType(contactDetails.getOwnerType());

        return contactDetailDto;
    }
}
