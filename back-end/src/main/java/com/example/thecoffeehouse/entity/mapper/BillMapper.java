package com.example.thecoffeehouse.entity.mapper;

import com.example.thecoffeehouse.dto.bill.BillDto;
import com.example.thecoffeehouse.dto.bill.BillProductDto;
import com.example.thecoffeehouse.entity.bill.Bill;
import com.example.thecoffeehouse.entity.bill.BillProduct;
import com.example.thecoffeehouse.entity.product.Product;
import com.example.thecoffeehouse.entity.product.ProductDetail;
import com.example.thecoffeehouse.entity.product.Topping;

import java.util.List;
import java.util.stream.Collectors;

public class BillMapper {
    public static Bill mapToBill(BillDto billDto) {
        Bill bill = new Bill();

        bill.setId(billDto.getId());
        bill.setUserID(billDto.getUserID());
        bill.setVoucherID(billDto.getVoucherID());
        bill.setValue(billDto.getValue());
        bill.setValueOfVoucher(billDto.getValueOfVoucher());
        bill.setValueOfCustomerPoint(billDto.getValueOfCustomerPoint());
        bill.setTotalValue(billDto.getTotalValue());
        bill.setCode(billDto.getCode());
        bill.setPaymentMethod(billDto.getPaymentMethod());
        bill.setPaymentStatus(billDto.getPaymentStatus());
        bill.setAddress(billDto.getAddress());
        bill.setDeliveryStatus(billDto.getDeliveryStatus());
        bill.setRate(billDto.getRate());
        bill.setComment(billDto.getComment());
        bill.setName(billDto.getName());
        bill.setPhoneNumber(billDto.getPhoneNumber());
        bill.setCreateTime(billDto.getCreateTime());
        bill.setModifyTime(billDto.getModifyTime());

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
        billDto.setComment(bill.getComment());
        billDto.setRate(bill.getRate());
        billDto.setName(bill.getName());
        billDto.setPhoneNumber(bill.getPhoneNumber());
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
        billProductDto.setProductSizeID(billProduct.getProductSizeID()); // Chỉnh sửa lỗi này
        billProductDto.setToppingID(billProduct.getToppingID()); // Chỉnh sửa lỗi này
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
