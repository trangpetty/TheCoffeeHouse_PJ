package com.example.thecoffeehouse.service.impl.bill;

import com.example.thecoffeehouse.Utils.DateTimeConverter;
import com.example.thecoffeehouse.dto.bill.BillDto;
import com.example.thecoffeehouse.dto.bill.BillProductDto;
import com.example.thecoffeehouse.dto.MonthlyDataDTO;
import com.example.thecoffeehouse.entity.bill.Bill;
import com.example.thecoffeehouse.entity.bill.BillProduct;
import com.example.thecoffeehouse.entity.mapper.BillMapper;
import com.example.thecoffeehouse.entity.product.Product;
import com.example.thecoffeehouse.entity.product.ProductDetail;
import com.example.thecoffeehouse.entity.product.Topping;
import com.example.thecoffeehouse.repository.bill.BillProductRepository;
import com.example.thecoffeehouse.repository.bill.BillRepository;
import com.example.thecoffeehouse.repository.product.ProductDetailRepository;
import com.example.thecoffeehouse.repository.product.ProductRepository;
import com.example.thecoffeehouse.repository.product.ToppingRepository;
import com.example.thecoffeehouse.service.bill.BillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {
    private static final Logger log = LoggerFactory.getLogger(BillServiceImpl.class);
    private final BillRepository billRepository;
    private final BillProductRepository billProductRepository;
    private final DateTimeConverter dateTimeConverter;
    private final ProductRepository productRepository;
    private final ToppingRepository toppingRepository;
    private final ProductDetailRepository productDetailRepository;

    public BillServiceImpl(BillRepository billRepository, BillProductRepository billProductRepository, DateTimeConverter dateTimeConverter, ProductRepository productRepository, ToppingRepository toppingRepository, ProductDetailRepository productDetailRepository) {
        this.billRepository = billRepository;
        this.billProductRepository = billProductRepository;
        this.dateTimeConverter = dateTimeConverter;
        this.productRepository = productRepository;
        this.toppingRepository = toppingRepository;
        this.productDetailRepository = productDetailRepository;
    }

    @Override
    public Page<BillDto> getAllBills(String code, String status, Date from, Date to, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDayOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime lastDayOfMonth = now.withDayOfMonth(YearMonth.from(now).lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);

        LocalDateTime applyFromConverted = from != null ?
                dateTimeConverter.convertToDateViaInstant(from) : firstDayOfMonth;
        LocalDateTime applyToConverted = to != null ?
                dateTimeConverter.convertToDateViaInstant(to) : lastDayOfMonth;

        List<Product> products = productRepository.findAll();
        List<Topping> toppings = toppingRepository.findAll();
        List<ProductDetail> sizes = productDetailRepository.findAll();


        Page<Bill> bills = billRepository.getAllBills(code, status, applyFromConverted, applyToConverted, pageable);
        return bills.map( bill -> {
            List<BillProduct> billProducts = billProductRepository.getBillProductByBillID(bill.getId());
            List<BillProductDto> billProductDtos = BillMapper.mapToBillProductsDto(billProducts, products, toppings, sizes);
            return BillMapper.mapToBillDto(bill, billProductDtos);

        });
    }

    @Override
    public BillDto createBill(BillDto billDto) {
        Bill bill = BillMapper.mapToBill(billDto);
        Bill savedBill = billRepository.save(bill);
        saveBillProducts(billDto.getProducts(), savedBill);
        List<BillProductDto> billProductDtos = billDto.getProducts();
        return BillMapper.mapToBillDto(savedBill, billProductDtos);
    }

    private void saveBillProducts(List<BillProductDto> billProductDtos, Bill bill) {
        for (BillProductDto billProductDto : billProductDtos) {
            BillProduct billProduct = new BillProduct();
            billProduct.setBillID(bill.getId());
            billProduct.setProductID(billProductDto.getProductID());
            billProduct.setProductSizeID(billProductDto.getProductSizeID());
            billProduct.setToppingID(billProductDto.getToppingID());
            billProduct.setQuantityProduct(billProductDto.getQuantityProduct());
            billProduct.setQuantityTopping(billProductDto.getQuantityTopping());
            billProduct.setCost(billProductDto.getCost());

            billProductRepository.save(billProduct);
        }
    }


    @Override
    public BillDto updateBill(String code, BillDto billDto) {
        Bill bill = billRepository.getBillByCode(code);
        bill.setComment(billDto.getComment());
        bill.setRate(billDto.getRate());

        Bill savedBill = billRepository.save(bill);

        List<Product> products = productRepository.findAll();
        List<Topping> toppings = toppingRepository.findAll();
        List<ProductDetail> sizes = productDetailRepository.findAll();

        List<BillProduct> billProducts = billProductRepository.getBillProductByBillID(bill.getId());
        List<BillProductDto> billProductDtos = BillMapper.mapToBillProductsDto(billProducts, products, toppings, sizes);
        return BillMapper.mapToBillDto(savedBill, billProductDtos);
    }

    @Override
    public BillDto payWithCash(Long billID, String cashTransactionId) {
        return null;
    }

    @Override
    public BillDto markAsDelivered(Long orderId) {
        return null;
    }

    public List<Double> getRevenue() {
        LocalDate currentDate = LocalDate.now();
        int currentMonthValue = currentDate.getMonthValue(); // Current month value (1 to 12)
        List<Double> revenues = new ArrayList<>();
        for (int i = 1; i <= currentMonthValue; i++) {
            if(billRepository.findRevenueByMonth(i) == null) {
                revenues.add(0.0);
            }
            else revenues.add(billRepository.findRevenueByMonth(i));
        }
        return revenues;
    }

    @Override
    public List<MonthlyDataDTO> getRevenueByMonth(int year) {
        return billRepository.getMonthlyRevenue(year);
    }

    @Override
    public BillDto getBillByCode(String code) {
        Bill bill = billRepository.getBillByCode(code);
        List<BillProduct> billProducts = billProductRepository.getBillProductByBillID(bill.getId());

        List<Product> products = productRepository.findAll();
        List<Topping> toppings = toppingRepository.findAll();
        List<ProductDetail> sizes = productDetailRepository.findAll();

        List<BillProductDto> billProductDto = BillMapper.mapToBillProductsDto(billProducts, products, toppings, sizes);
        return BillMapper.mapToBillDto(bill, billProductDto);
    }

    @Override
    public void updatePaymentStatus(String code, int status) {
        Bill bill = billRepository.findByCode(code);
        if (bill != null) {
            bill.setPaymentStatus(status);
            if(status == 0 ){
                bill.setStatus("success");
            } else if (status == 1) {
                bill.setStatus("fail");
            }
            billRepository.save(bill);
        }
    }

}
