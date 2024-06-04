package com.example.thecoffeehouse.service.bill;

import com.example.thecoffeehouse.entity.bill.BillProduct;

import java.util.List;

public interface BillProductService {
    List<BillProduct> getBillProductByBillID(Long id);

    BillProduct createBillProduct(BillProduct billProduct);
}
