package com.example.thecoffeehouse.service.impl.bill;

import com.example.thecoffeehouse.entity.bill.Bill;
import com.example.thecoffeehouse.entity.bill.BillProduct;
import com.example.thecoffeehouse.repository.bill.BillProductRepository;
import com.example.thecoffeehouse.repository.bill.BillRepository;
import com.example.thecoffeehouse.service.bill.BillProductService;
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
            return billProductRepository.getBillProductByBillID(id);
        }
        return null;
    }

    @Override
    public BillProduct createBillProduct(BillProduct billProduct) {
        return billProductRepository.save(billProduct);
    }
}
