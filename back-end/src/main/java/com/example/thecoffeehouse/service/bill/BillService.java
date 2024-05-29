package com.example.thecoffeehouse.service.bill;

import com.example.thecoffeehouse.dto.BillDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface BillService {
    Page<BillDto> getAllBills(String code, int status, Date from, Date to, Pageable pageable);

    BillDto createBill(BillDto billDto);

    BillDto updateBill(Long id, BillDto billDto);

}
