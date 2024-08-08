package com.example.thecoffeehouse.dto.user;

import com.example.thecoffeehouse.dto.VoucherDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VoucherRequest {
    private VoucherDto voucherDto;
    private List<Long> voucherProducts;

}

