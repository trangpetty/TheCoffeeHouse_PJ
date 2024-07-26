package com.example.thecoffeehouse.repository;

import com.example.thecoffeehouse.entity.user.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface UserRepository extends JpaRepository<User, Long>{
    @Query("SELECT u " +
            "FROM User u " +
            "WHERE (:name IS NULL OR u.firstName LIKE %:name%) " +
            "AND (:phoneNumber IS NULL OR u.phoneNumber LIKE %:phoneNumber%)")
    Page<User> getAllByNameAndPhoneNumber(@Param("name") String name, @Param("phoneNumber") String phoneNumber, Pageable pageable);

    User findByEmail(@Param("email") String email);

    User findByRole(Role role);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.createTime BETWEEN :startOfDay AND :endOfDay")
    Integer findNewUsersToday(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

}
