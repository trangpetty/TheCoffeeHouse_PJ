package com.example.thecoffeehouse.service.impl.bill;

import com.example.thecoffeehouse.Utils.DateTimeConverter;
import com.example.thecoffeehouse.dto.BillDto;
import com.example.thecoffeehouse.dto.BillProductDto;
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

import java.time.LocalDateTime;
import java.time.YearMonth;
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
    public Page<BillDto> getAllBills(String code, int status, Date from, Date to, Pageable pageable) {
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
    public BillDto updateBill(Long id, BillDto billDto) {
        Bill bill = billRepository.
                findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
//        bill.setStatus(billDto.getStatus());

        Bill savedBill = billRepository.save(bill);
        return null;
//        return BillMapper.mapToBillDto(savedBill);
    }

    @Override
    public BillDto payWithMoMo(Long billID, String qrCodeTransactionId) {
        Bill bill = billRepository.findById(billID).orElse(null);
        if (bill == null) {
            // Handle case where bill is not found
            return null;
        }

        List<Product> products = productRepository.findAll();
        List<Topping> toppings = toppingRepository.findAll();
        List<ProductDetail> sizes = productDetailRepository.findAll();

        List<BillProduct> billProducts = billProductRepository.getBillProductByBillID(bill.getId());
        List<BillProductDto> billProductDtos = BillMapper.mapToBillProductsDto(billProducts, products, toppings, sizes);
        return BillMapper.mapToBillDto(bill, billProductDtos);
    }

    @Override
    public BillDto payWithCash(Long billID, String cashTransactionId) {
        return null;
    }

    @Override
    public BillDto markAsDelivered(Long orderId) {
        return null;
    }
}
