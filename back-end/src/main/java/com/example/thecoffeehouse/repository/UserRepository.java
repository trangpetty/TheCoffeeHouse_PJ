package com.example.thecoffeehouse.repository;

import com.example.thecoffeehouse.entity.user.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @Query("SELECT COUNT(u) FROM User u WHERE u.createTime BETWEEN :startOfDay AND :endOfDay")
    Integer findNewUsersToday(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT u FROM User u WHERE u.dob BETWEEN :startDate AND :endDate")
    List<User> findUsersByAgeRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT u, COUNT(b.id) AS totalOrders, " +
            "SUM(CASE WHEN b.status = 'success' THEN 1 ELSE 0 END) AS successfulOrders, " +
            "SUM(CASE WHEN b.status = 'cancel' THEN 1 ELSE 0 END) AS canceledOrders, " +
            "SUM(CASE WHEN b.status = 'fail' THEN 1 ELSE 0 END) AS failedOrders " +
            "FROM User u JOIN Bill b ON u.id = b.userID " +
            "GROUP BY u.id " +
            "ORDER BY successfulOrders DESC")
    List<Object[]> findTopUsersBuy(Pageable pageable);

    @Query("SELECT u, COUNT(b.id) AS totalOrders, " +
            "SUM(CASE WHEN b.status = 'success' THEN 1 ELSE 0 END) AS successfulOrders, " +
            "SUM(CASE WHEN b.status = 'cancel' THEN 1 ELSE 0 END) AS canceledOrders, " +
            "SUM(CASE WHEN b.status = 'fail' THEN 1 ELSE 0 END) AS failedOrders " +
            "FROM User u JOIN Bill b ON u.id = b.userID " +
            "GROUP BY u.id " +
            "ORDER BY failedOrders DESC, canceledOrders DESC")
    List<Object[]> findTopUsersCancelOrder(Pageable pageable);

}
