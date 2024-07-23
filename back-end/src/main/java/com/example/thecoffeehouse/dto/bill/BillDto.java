package com.example.thecoffeehouse.dto.bill;

import com.example.thecoffeehouse.dto.user.ContactDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillDto {
    private Long id;
    private Long userID;
    private Long customerID;
    private Long voucherID;
    private double value;
    private double valueOfVoucher;
    private int valueOfCustomerPoint;
    private int usedCustomerPoints;
    private double totalValue;
    private String code;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
    private List<BillProductDto> products;
    private String paymentMethod;
    private int paymentStatus;
    private String deliveryStatus;
    private int rate;
    private String comment;
    private ContactDetailDto contactDetail;
}
