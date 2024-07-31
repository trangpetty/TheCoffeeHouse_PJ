package com.example.thecoffeehouse.service.impl.bill;

import com.example.thecoffeehouse.Utils.DateTimeConverter;
import com.example.thecoffeehouse.dto.bill.BillDto;
import com.example.thecoffeehouse.dto.bill.BillProductDto;
import com.example.thecoffeehouse.dto.bill.RevenueDTO;
import com.example.thecoffeehouse.dto.user.ContactDetailDto;
import com.example.thecoffeehouse.entity.bill.Bill;
import com.example.thecoffeehouse.entity.bill.BillProduct;
import com.example.thecoffeehouse.entity.mapper.BillMapper;
import com.example.thecoffeehouse.entity.mapper.ContactDetailMapper;
import com.example.thecoffeehouse.entity.product.Product;
import com.example.thecoffeehouse.entity.product.ProductDetail;
import com.example.thecoffeehouse.entity.product.Topping;
import com.example.thecoffeehouse.entity.user.ContactDetails;
import com.example.thecoffeehouse.entity.user.Customer;
import com.example.thecoffeehouse.entity.user.OwnerType;
import com.example.thecoffeehouse.entity.user.User;
import com.example.thecoffeehouse.repository.ContactDetailRepository;
import com.example.thecoffeehouse.repository.CustomerRepository;
import com.example.thecoffeehouse.repository.UserRepository;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements BillService {
    private static final Logger log = LoggerFactory.getLogger(BillServiceImpl.class);
    private final BillRepository billRepository;
    private final BillProductRepository billProductRepository;
    private final DateTimeConverter dateTimeConverter;
    private final ProductRepository productRepository;
    private final ToppingRepository toppingRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ContactDetailRepository contactDetailRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public BillServiceImpl(BillRepository billRepository, BillProductRepository billProductRepository, DateTimeConverter dateTimeConverter, ProductRepository productRepository, ToppingRepository toppingRepository, ProductDetailRepository productDetailRepository, ContactDetailRepository contactDetailRepository, UserRepository userRepository, CustomerRepository customerRepository) {
        this.billRepository = billRepository;
        this.billProductRepository = billProductRepository;
        this.dateTimeConverter = dateTimeConverter;
        this.productRepository = productRepository;
        this.toppingRepository = toppingRepository;
        this.productDetailRepository = productDetailRepository;
        this.contactDetailRepository = contactDetailRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
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
            if(bill.getContactDetailID() != null) {
//                ContactDetails contactDetails = contactDetailRepository
//                                                .findById(bill.getContactDetailID())
//                                                .orElseThrow(() -> new RuntimeException("Contact not found"));
//                ContactDetailDto contactDetailDto = new ContactDetailDto();
//                contactDetailDto.setId(contactDetails.getId());
//                contactDetailDto.setPhoneNumber(contactDetails.getPhoneNumber());
//                contactDetailDto.setName(contactDetails.getName());
//                contactDetailDto.setAddress(contactDetails.getAddress());
//                contactDetailDto.setOwnerID(contactDetails.getOwnerID());
//                contactDetailDto.setOwnerType(contactDetails.getOwnerType());
                ContactDetails contactDetails = contactDetailRepository.findById(bill.getContactDetailID()).orElseThrow(() -> new RuntimeException("ContactDetail does not exist"));
                ContactDetailDto contactDetailDto = ContactDetailMapper.mapToContactDetailDto(contactDetails);
                return BillMapper.mapToBillDto(bill, billProductDtos, contactDetailDto);
            }
            return BillMapper.mapToBillDto(bill, billProductDtos);

        });
    }

    @Override
    public BillDto createBill(BillDto billDto) {
        // Kiểm tra và thiết lập ownerID và ownerType cho ContactDetailDto
        if (billDto.getContactDetail().getOwnerType() == OwnerType.USER) {
            User user = userRepository
                    .findById(billDto.getContactDetail().getOwnerID())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            billDto.getContactDetail().setOwnerID(user.getId());
            billDto.getContactDetail().setOwnerType(OwnerType.USER);
        } else {
            Customer customer = customerRepository.findByPhoneNumber(billDto.getContactDetail().getPhoneNumber());
            if (customer == null) {
                customer = new Customer();
                customer.setPhoneNumber(billDto.getContactDetail().getPhoneNumber());
                customer.setDefaultName(billDto.getContactDetail().getName());
                customer.setDefaultAddress(billDto.getContactDetail().getAddress());
                customer.setPoint(billDto.getValueOfCustomerPoint());
                customerRepository.save(customer);
            }
            billDto.setCustomerID(customer.getId());
            billDto.getContactDetail().setOwnerID(customer.getId());
            billDto.getContactDetail().setOwnerType(OwnerType.CUSTOMER);

        }

        // Tạo hoặc cập nhật ContactDetails
        ContactDetailDto contactDetailDto = createOrUpdateContactDetails(billDto.getContactDetail());
        billDto.setContactDetail(contactDetailDto);

        // Chuyển đổi BillDto sang Bill và thiết lập contactDetailID
        Bill bill = BillMapper.mapToBill(billDto);
        bill.setContactDetailID(contactDetailDto.getId());

        // Lưu bill và các chi tiết liên quan
        Bill savedBill = billRepository.save(bill);
        saveBillProducts(billDto.getProducts(), savedBill);
        List<BillProductDto> billProductDtos = billDto.getProducts();

        return BillMapper.mapToBillDto(savedBill, billProductDtos, contactDetailDto);
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
    public List<RevenueDTO> getRevenueByType(String type, Integer month, Integer week) {
        if ("daily".equalsIgnoreCase(type) && month != null && week != null) {
            return billRepository.findDailyRevenueForWeekInMonth(month, week);
        }
        // Other types of revenue queries
        return switch (type) {
            case "weekly" -> getWeeklyRevenue();
            case "yearly" -> getYearlyRevenue();
//            case "productType":
//                return getRevenueByProductType();
            default -> getMonthlyRevenue();
        };
    }

    @Override
    public BillDto getBillByCode(String code) {
        Bill bill = billRepository.getBillByCode(code);
        List<BillProduct> billProducts = billProductRepository.getBillProductByBillID(bill.getId());

        List<Product> products = productRepository.findAll();
        List<Topping> toppings = toppingRepository.findAll();
        List<ProductDetail> sizes = productDetailRepository.findAll();

        List<BillProductDto> billProductDto = BillMapper.mapToBillProductsDto(billProducts, products, toppings, sizes);

        ContactDetails contactDetails = contactDetailRepository.findById(bill.getContactDetailID()).orElseThrow(() -> new RuntimeException("ContactDetail does not exist"));
        ContactDetailDto contactDetailDto = ContactDetailMapper.mapToContactDetailDto(contactDetails);

        return BillMapper.mapToBillDto(bill, billProductDto, contactDetailDto);
    }

    @Override
    public void updatePaymentStatus(String code, int status) {
        Bill bill = billRepository.findByCode(code);
        if (bill != null) {
            bill.setPaymentStatus(status);
            if (status == 0) { // Thanh toán thành công
                bill.setStatus("pending");
                log.info("Bill status before save: {}", bill.getStatus());
                if (bill.getUserID() != null) {
                    User user = userRepository.findById(bill.getUserID())
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    int currentPoints = user.getPoint();

                    user.setPoint(currentPoints - bill.getUsedCustomerPoints() + bill.getValueOfCustomerPoint());
                    userRepository.save(user);

                    log.info("Updated points for user: {}", currentPoints);
                }
                else {
                    Customer customer = customerRepository.findById(bill.getCustomerID())
                            .orElseThrow(() -> new RuntimeException("Customer not found"));

                    int currentPoints = customer.getPoint();
                    customer.setPoint(currentPoints - bill.getUsedCustomerPoints() + bill.getValueOfCustomerPoint());
                    customerRepository.save(customer);

                    log.info("Updated points for customer: {}", customer.getId());
                }
            } else if (status == 1) { // Thanh toán thất bại
                bill.setStatus("fail");
            }
            billRepository.save(bill);
            log.info("Bill status after save: {}", bill.getStatus());
        } else {
            log.warn("Bill with code {} not found", code);
        }
    }

    public void updateDeliveryStatus(String code, String deliveryStatus) {
        Bill bill = billRepository.findByCode(code);
        if(bill != null) {
            bill.setDeliveryStatus(deliveryStatus);
            if(Objects.equals(deliveryStatus, "Delivered")) {
                bill.setStatus("success");
            }
            billRepository.save(bill);
        }
    }

    @Override
    public List<BillDto> getBillsByUserId(Long userID) {
        List<Bill> bills = billRepository.findByUserIDOrderByModifyTimeDesc(userID);

        List<Product> products = productRepository.findAll();
        List<Topping> toppings = toppingRepository.findAll();
        List<ProductDetail> sizes = productDetailRepository.findAll();

        return bills.stream().map( bill -> {
            List<BillProduct> billProducts = billProductRepository.getBillProductByBillID(bill.getId());
            List<BillProductDto> billProductDtos = BillMapper.mapToBillProductsDto(billProducts, products, toppings, sizes);
            return BillMapper.mapToBillDto(bill, billProductDtos);
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getTodayStatistics() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        BigDecimal totalRevenue = billRepository.calculateTotalRevenue(startOfDay, endOfDay);
        Integer totalProductsSold = billProductRepository.calculateTotalProductsSold(startOfDay, endOfDay);
        Integer totalOrders = billRepository.countTotalOrders(startOfDay, endOfDay);
        Integer newCustomers = customerRepository.findNewCustomersToday(startOfDay, endOfDay) + userRepository.findNewUsersToday(startOfDay, endOfDay);

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalRevenue", totalRevenue != null ? totalRevenue : BigDecimal.ZERO);
        statistics.put("totalProductsSold", totalProductsSold != null ? totalProductsSold : 0);
        statistics.put("totalOrders", totalOrders != null ? totalOrders : 0);
        statistics.put("newCustomers", newCustomers != null ? newCustomers : 0);

        return statistics;
    }

    @Override
    public Double totalValueByUserIDForCurrentYear(Long userID) {
        return billRepository.findTotalValueByUserIDForCurrentYear(userID);
    }

    private ContactDetailDto createOrUpdateContactDetails(ContactDetailDto contactDetailDto) {
        ContactDetails contactDetails = contactDetailRepository.findByPhoneNumberAndAddressAndName(
                contactDetailDto.getPhoneNumber(), contactDetailDto.getAddress(), contactDetailDto.getName());

        if (contactDetails != null) {
            // Cập nhật thông tin liên hệ hiện có
            contactDetails.setName(contactDetailDto.getName());
            contactDetails.setAddress(contactDetailDto.getAddress());
            contactDetailRepository.save(contactDetails);
        } else {
            // Tạo mới thông tin liên hệ
            contactDetails = new ContactDetails();
            contactDetails.setOwnerID(contactDetailDto.getOwnerID());
            contactDetails.setOwnerType(contactDetailDto.getOwnerType());
            contactDetails.setPhoneNumber(contactDetailDto.getPhoneNumber());
            contactDetails.setName(contactDetailDto.getName());
            contactDetails.setAddress(contactDetailDto.getAddress());
            contactDetailRepository.save(contactDetails);
        }

        // Chuyển đổi thành DTO để trả về
        ContactDetailDto updatedContactDetailDto = new ContactDetailDto();
        updatedContactDetailDto.setId(contactDetails.getId());
        updatedContactDetailDto.setOwnerID(contactDetails.getOwnerID());
        updatedContactDetailDto.setOwnerType(contactDetails.getOwnerType());
        updatedContactDetailDto.setPhoneNumber(contactDetails.getPhoneNumber());
        updatedContactDetailDto.setName(contactDetails.getName());
        updatedContactDetailDto.setAddress(contactDetails.getAddress());

        return updatedContactDetailDto;
    }

    private List<RevenueDTO> getMonthlyRevenue() {
        // Giả sử phương thức này trả về danh sách doanh thu hàng tháng
        List<RevenueDTO> revenues = billRepository.findMonthlyRevenue();
        return revenues.stream()
                .map(revenue -> new RevenueDTO(revenue.getLabel(), revenue.getValue()))
                .collect(Collectors.toList());
    }

    private List<RevenueDTO> getWeeklyRevenue() {
        // Giả sử phương thức này trả về danh sách doanh thu hàng tuần
        List<RevenueDTO> revenues = billRepository.findWeeklyRevenue();
        return revenues.stream()
                .map(revenue -> new RevenueDTO(revenue.getLabel(), revenue.getValue()))
                .collect(Collectors.toList());
    }

    private List<RevenueDTO> getYearlyRevenue() {
        // Giả sử phương thức này trả về danh sách doanh thu hàng năm
        List<RevenueDTO> revenues = billRepository.findYearlyRevenue();
        return revenues.stream()
                .map(revenue -> new RevenueDTO(revenue.getLabel(), revenue.getValue()))
                .collect(Collectors.toList());
    }

//    public List<RevenueDTO> getDailyRevenueForWeekInMonth(int month, int week) {
//        List<RevenueDTO> revenues = billRepository.findDailyRevenueForWeekInMonth(month, week);
//        Map<String, Double> revenueMap = new HashMap<>();
//        revenues.forEach(revenue -> revenueMap.put(revenue.getLabel(), revenue.getValue()));
//
//        List<RevenueDTO> result = new ArrayList<>();
//        for (DayOfWeek day : DayOfWeek.values()) {
//            String dayName = day.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
//            result.add(new RevenueDTO(dayName, revenueMap.getOrDefault(dayName, 0.0)));
//        }
//        log.info("revenue: {}", revenues);
//        return result;
//    }

//    private List<RevenueDTO> getRevenueByProductType() {
//        // Giả sử phương thức này trả về danh sách doanh thu theo loại sản phẩm
//        List<RevenueDTO> revenues = billRepository.findRevenueByProductType();
//        return revenues.stream()
//                .map(revenue -> new RevenueDTO(revenue.getLabel(), revenue.getValue()))
//                .collect(Collectors.toList());
//    }
}
