package com.example.thecoffeehouse.service;

import com.example.thecoffeehouse.entity.BillProduct;

import java.util.List;

public interface BillProductService {
    List<BillProduct> getBillProductByBillID(String id);

    BillProduct createBillProduct(BillProduct billProduct);
}
