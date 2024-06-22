package com.example.thecoffeehouse.repository;

import com.example.thecoffeehouse.dto.user.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>{
    @Query("SELECT new com.example.thecoffeehouse.dto.user.UserDto(u.id, u.name, u.phoneNumber, u.email, u.avatar, u.gender, u.createTime, u.modifyTime, r.name, r.removable) " +
            "FROM User u " +
            "JOIN UserRole ur ON u.id = ur.userID " +
            "JOIN Role r ON ur.roleID = r.id " +
            "WHERE (:name IS NULL OR u.name LIKE %:name%) AND (:phoneNumber IS NULL OR u.phoneNumber LIKE %:phoneNumber%)")
    Page<UserDto> getAllByNameAndPhoneNumber(@Param("name") String name, @Param("phoneNumber") String phoneNumber, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(@Param("email") String email);

}
