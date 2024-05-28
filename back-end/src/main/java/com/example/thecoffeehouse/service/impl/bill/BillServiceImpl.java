package com.example.thecoffeehouse.service.impl.bill;

import com.example.thecoffeehouse.DateTimeConverter;
import com.example.thecoffeehouse.dto.BillDto;
import com.example.thecoffeehouse.dto.BillProductDto;
import com.example.thecoffeehouse.entity.Bill;
import com.example.thecoffeehouse.entity.BillProduct;
import com.example.thecoffeehouse.entity.mapper.BillMapper;
import com.example.thecoffeehouse.entity.product.Product;
import com.example.thecoffeehouse.repository.bill.BillProductRepository;
import com.example.thecoffeehouse.repository.bill.BillRepository;
import com.example.thecoffeehouse.repository.product.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final BillProductRepository billProductRepository;
    private final DateTimeConverter dateTimeConverter;
    private final ProductRepository productRepository;


    public BillServiceImpl(BillRepository billRepository, BillProductRepository billProductRepository, DateTimeConverter dateTimeConverter, ProductRepository productRepository) {
        this.billRepository = billRepository;
        this.billProductRepository = billProductRepository;
        this.dateTimeConverter = dateTimeConverter;
        this.productRepository = productRepository;
    }

    @Override
    public Page<BillDto> getAllBills(String code, int status, Date from, Date to, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDayOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime lastDayOfMonth = now.withDayOfMonth(YearMonth.from(now).lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);

        LocalDateTime applyFromConverted = from != null ?
                dateTimeConverter.convertToDateViaInstant(from) : firstDayOfMonth;
        LocalDateTime applyToConverted = to != null ?
                dateTimeConverter.convertToDateViaInstant(to) : lastDayOfMonth;
        Page<Bill> bills = billRepository.getAllBills(code, status, applyFromConverted, applyToConverted, pageable);
        return bills.map( bill -> {
            List<BillProduct> billProducts = billProductRepository.getBillProductByBillID(bill.getId());
            List<BillProductDto> billProductDtos = billProducts.stream()
                    .map(billProduct -> {
                        BillProductDto billProductDto = BillMapper.mapToBillProductDto(billProduct);
                        if(billProduct.getProductID() != null) {
                            Product product = productRepository.findById(billProduct.getProductID())
                                    .orElseThrow(() -> new RuntimeException("Product does not exist"));
                            billProductDto.setProductName(product.getName());
                        }
                        return billProductDto;
                    })
                    .collect(Collectors.toList());
            return BillMapper.mapToBillDto(bill, billProductDtos);

        });
    }

    @Override
    public BillDto createBill(BillDto billDto) {
        Bill bill = BillMapper.mapToBill(billDto);
        Bill savedBill = billRepository.save(bill);
//        return BillMapper.mapToBillDto(savedBill);
        return null;
    }

    @Override
    public BillDto updateBill(Long id, BillDto billDto) {
        Bill bill = billRepository.
                findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        bill.setStatus(billDto.getStatus());

        Bill savedBill = billRepository.save(bill);
        return null;
//        return BillMapper.mapToBillDto(savedBill);
    }
}
