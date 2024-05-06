package com.example.thecoffeehouse.service.impl;

import com.example.thecoffeehouse.entity.Bill;
import com.example.thecoffeehouse.entity.BillProduct;
import com.example.thecoffeehouse.repository.BillProductRepository;
import com.example.thecoffeehouse.repository.BillRepository;
import com.example.thecoffeehouse.service.BillProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillProductServiceImpl implements BillProductService {

    private final BillProductRepository billProductRepository;
    private final BillRepository billRepository;

    public BillProductServiceImpl(BillProductRepository billProductRepository, BillRepository billRepository) {
        this.billProductRepository = billProductRepository;
        this.billRepository = billRepository;
    }

    @Override
    public List<BillProduct> getBillProductByBillID(Long id) {
        Bill bill = billRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        if(bill != null) {
            List billProducts = billProductRepository.getBillProductByBillID(id);
            return billProducts;
        }
        return null;
    }

    @Override
    public BillProduct createBillProduct(BillProduct billProduct) {
        BillProduct savedBillProduct = billProductRepository.save(billProduct);
        return savedBillProduct;
    }
}
