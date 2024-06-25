package com.example.thecoffeehouse.entity.mapper;

import com.example.thecoffeehouse.dto.BillDto;
import com.example.thecoffeehouse.dto.BillProductDto;
import com.example.thecoffeehouse.entity.bill.Bill;
import com.example.thecoffeehouse.entity.bill.BillProduct;
import com.example.thecoffeehouse.entity.product.Product;
import com.example.thecoffeehouse.entity.product.ProductDetail;
import com.example.thecoffeehouse.entity.product.Topping;

import java.util.List;
import java.util.stream.Collectors;

public class BillMapper {
    public static Bill mapToBill(BillDto billDto) {
        Bill bill = new Bill(
                billDto.getId(),
                billDto.getUserID(),
                billDto.getVoucherID(),
                billDto.getValue(),
                billDto.getValueOfVoucher(),
                billDto.getValueOfCustomerPoint(),
                billDto.getTotalValue(),
                billDto.getCode(),
                billDto.getPaymentMethod(),
                billDto.getPaymentStatus(),
                billDto.getAddress(),
                billDto.getDeliveryStatus(),
                billDto.getCreateTime(),
                billDto.getModifyTime()
        );

        return bill;
    }

    public static BillDto mapToBillDto(Bill bill, List<BillProductDto> billProductDtos) {
        BillDto billDto = new BillDto();
        billDto.setId(bill.getId());
        billDto.setUserID(bill.getUserID());
        billDto.setVoucherID(bill.getVoucherID());
        billDto.setValue(bill.getValue());
        billDto.setValueOfVoucher(bill.getValueOfVoucher());
        billDto.setValueOfCustomerPoint(bill.getValueOfCustomerPoint());
        billDto.setTotalValue(bill.getTotalValue());
        billDto.setCode(bill.getCode());
        billDto.setAddress(bill.getAddress());
        billDto.setCreateTime(bill.getCreateTime());
        billDto.setModifyTime(bill.getModifyTime());
        billDto.setProducts(billProductDtos);
        billDto.setPaymentMethod(bill.getPaymentMethod());
        billDto.setPaymentStatus(bill.getPaymentStatus());
        billDto.setDeliveryStatus(bill.getDeliveryStatus());
        return billDto;
    }

    public static List<BillProductDto> mapToBillProductsDto(List<BillProduct> billProducts, List<Product> products, List<Topping> toppings, List<ProductDetail> sizes) {
        return billProducts.stream()
                .map(billProduct -> {
                    Product product = products.stream()
                            .filter(p -> p.getId().equals(billProduct.getProductID()))
                            .findFirst()
                            .orElse(null);
                    Topping topping = toppings.stream()
                            .filter(t -> t.getId().equals(billProduct.getToppingID()))
                            .findFirst()
                            .orElse(null);
                    ProductDetail size = sizes.stream()
                            .filter(s -> s.getId().equals(billProduct.getProductSizeID()))
                            .findFirst()
                            .orElse(null);
                    return mapToBillProductDto(billProduct, product, topping, size);
                })
                .collect(Collectors.toList());
    }

    public static BillProductDto mapToBillProductDto(BillProduct billProduct, Product product, Topping topping, ProductDetail size) {
        BillProductDto billProductDto = new BillProductDto();
        billProductDto.setId(billProduct.getId());
        billProductDto.setProductID(billProduct.getProductID());
        billProductDto.setProductSizeID(billProductDto.getProductSizeID());
        billProductDto.setToppingID(billProductDto.getToppingID());
        if(product != null) {
            billProductDto.setProductName(product.getName());
            billProductDto.setPriceProduct(product.getPrice());
        }
        if(topping != null) {
            billProductDto.setToppingName(topping.getName());
            billProductDto.setPriceTopping(topping.getPrice());
        }
        if(size != null) {
            billProductDto.setProductSize(size.getSize());
            billProductDto.setSurcharge(size.getSurcharge());
        }
        billProductDto.setQuantityProduct(billProduct.getQuantityProduct());
        billProductDto.setQuantityTopping(billProduct.getQuantityTopping());
        billProductDto.setCost(billProduct.getCost());

        return billProductDto;
    }
}
