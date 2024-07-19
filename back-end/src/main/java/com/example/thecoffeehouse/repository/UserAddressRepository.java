package com.example.thecoffeehouse.repository;

import com.example.thecoffeehouse.entity.user.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    boolean existsByUserIdAndAddress(Long userId, String address);

    @Query(value = "SELECT * FROM user_address WHERE user_id = :userId ORDER BY id DESC LIMIT 1", nativeQuery = true)
    UserAddress findLastByUserId(@Param("userId") Long userId);

    List<UserAddress> findByUserId(Long userID);
}
