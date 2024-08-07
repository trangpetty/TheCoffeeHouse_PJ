package com.example.thecoffeehouse.repository;

import com.example.thecoffeehouse.entity.user.UserVoucher;
import com.example.thecoffeehouse.entity.voucher.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserVoucherRepository extends JpaRepository<UserVoucher, Long> {
    UserVoucher findByUserIDAndAndVoucherID(Long userID, Long voucherID);

    @Query("SELECT v FROM Voucher v JOIN UserVoucher uv ON v.id = uv.voucherID WHERE uv.userID = :userId")
    List<Voucher> findByUserID(@Param("userId") Long userID);
}
