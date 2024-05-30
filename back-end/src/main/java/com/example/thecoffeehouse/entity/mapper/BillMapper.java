package com.example.thecoffeehouse.entity.mapper;

import com.example.thecoffeehouse.dto.BillDto;
import com.example.thecoffeehouse.dto.BillProductDto;
import com.example.thecoffeehouse.entity.Bill;
import com.example.thecoffeehouse.entity.BillProduct;
import com.example.thecoffeehouse.entity.product.Product;
import com.example.thecoffeehouse.entity.product.ProductDetail;
import com.example.thecoffeehouse.entity.product.Topping;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class BillMapper {
    public static Bill mapToBill(BillDto billDto) {
        Bill bill = new Bill(
                billDto.getId(),
                billDto.getCustomerID(),
                billDto.getVoucherID(),
                billDto.getValue(),
                billDto.getValueOfVoucher(),
                billDto.getValueOfCustomerPoint(),
                billDto.getTotalValue(),
                billDto.getCode(),
                billDto.getPoint(),
                billDto.getStatus(),
                billDto.getAddress(),
                billDto.getCreateTime(),
                billDto.getModifyTime()
        );

        return bill;
    }

    public static BillDto mapToBillDto(Bill bill, List<BillProductDto> billProductDtos) {
        BillDto billDto = new BillDto(
                bill.getId(),
                bill.getCustomerID(),
                bill.getVoucherID(),
                bill.getValue(),
                bill.getValueOfVoucher(),
                bill.getValueOfCustomerPoint(),
                bill.getTotalValue(),
                bill.getCode(),
                bill.getPoint(),
                bill.getStatus(),
                bill.getAddress(),
                bill.getCreateTime(),
                bill.getModifyTime(),
                billProductDtos
        );

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
        }
        if(topping != null) {
            billProductDto.setToppingName(topping.getName());
        }
        if(size != null) {
            billProductDto.setProductSize(size.getSize());
        }
//        billProductDto.setQuantityProduct(billProduct.getQuantityProduct());
//        billProductDto.setQuantityTopping(billProduct.getQuantityTopping());
//        billProductDto.setCost(billProduct.getCost());

        return billProductDto;
    }
}
