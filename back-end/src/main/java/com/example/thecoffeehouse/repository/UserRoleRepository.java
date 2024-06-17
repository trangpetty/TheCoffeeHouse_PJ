package com.example.thecoffeehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.UserRole;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long>{
    @Query("SELECT ur FROM UserRole ur WHERE ur.userID = :userID")
    List<UserRole> findByUserID(@Param("userID") Long userID);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserRole ur WHERE ur.userID = :userID")
    void deleteByUserID(@Param("userID") Long userID);

    @Query("SELECT ur FROM UserRole ur WHERE ur.userID = :userID")
    List<UserRole> findByUserId(@Param("userID") Long userId);

}
