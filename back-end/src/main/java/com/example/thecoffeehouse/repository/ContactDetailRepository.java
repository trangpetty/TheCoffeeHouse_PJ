package com.example.thecoffeehouse.repository;

import com.example.thecoffeehouse.entity.user.ContactDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactDetailRepository extends JpaRepository<ContactDetails, Long> {
    ContactDetails findByPhoneNumberAndAddressAndName(String phoneNumber, String address, String name);

    @Query("SELECT ct FROM ContactDetails ct WHERE (ct.ownerID = :userId) AND (ct.ownerType = 0) ORDER BY ct.id DESC LIMIT 1")
    ContactDetails findLastByUserId(@Param("userId") Long userId);

    @Query("SELECT ct FROM ContactDetails ct WHERE (ct.ownerID = :userId) AND (ct.ownerType = 0) ORDER BY ct.id DESC")
    List<ContactDetails> findByUserId(@Param("userId") Long userId);
}
