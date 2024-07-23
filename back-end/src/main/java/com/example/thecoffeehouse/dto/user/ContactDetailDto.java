package com.example.thecoffeehouse.dto.user;

import com.example.thecoffeehouse.entity.user.OwnerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDetailDto {
    private Long id;
    private Long ownerID;
    private OwnerType ownerType;
    private String phoneNumber;
    private String name;
    private String address;
}
