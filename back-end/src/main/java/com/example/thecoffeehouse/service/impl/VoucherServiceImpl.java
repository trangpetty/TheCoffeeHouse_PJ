package com.example.thecoffeehouse.service.impl;

import com.example.thecoffeehouse.Utils.DateTimeConverter;
import com.example.thecoffeehouse.dto.VoucherDto;
import com.example.thecoffeehouse.dto.user.VoucherRequest;
import com.example.thecoffeehouse.entity.user.ContactDetails;
import com.example.thecoffeehouse.entity.user.OwnerType;
import com.example.thecoffeehouse.entity.user.User;
import com.example.thecoffeehouse.entity.user.UserVoucher;
import com.example.thecoffeehouse.entity.voucher.Voucher;
import com.example.thecoffeehouse.entity.mapper.VoucherMapper;
import com.example.thecoffeehouse.entity.voucher.VoucherProduct;
import com.example.thecoffeehouse.entity.voucher.VoucherType;
import com.example.thecoffeehouse.repository.*;
import com.example.thecoffeehouse.repository.bill.BillRepository;
import com.example.thecoffeehouse.repository.product.ProductDetailRepository;
import com.example.thecoffeehouse.repository.voucher.VoucherProductRepository;
import com.example.thecoffeehouse.repository.voucher.VoucherRepository;
import com.example.thecoffeehouse.repository.voucher.VoucherTypeRepository;
import com.example.thecoffeehouse.service.VoucherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VoucherServiceImpl implements VoucherService {
    private static final Logger log = LoggerFactory.getLogger(VoucherServiceImpl.class);
    private final VoucherRepository voucherRepository;
    private final VoucherTypeRepository voucherTypeRepository;
    private final DateTimeConverter dateTimeConverter;
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final UserVoucherRepository userVoucherRepository;
    private final ContactDetailRepository contactDetailRepository;
    private final VoucherProductRepository voucherProductRepository;
    private final ProductDetailRepository productDetailRepository;

    public VoucherServiceImpl(VoucherRepository voucherRepository, VoucherTypeRepository voucherTypeRepository, DateTimeConverter dateTimeConverter, BillRepository billRepository, UserRepository userRepository, CustomerRepository customerRepository, UserVoucherRepository userVoucherRepository, ContactDetailRepository contactDetailRepository, VoucherProductRepository voucherProductRepository, ProductDetailRepository productDetailRepository) {
        this.voucherRepository = voucherRepository;
        this.voucherTypeRepository = voucherTypeRepository;
        this.dateTimeConverter = dateTimeConverter;
        this.billRepository = billRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.userVoucherRepository = userVoucherRepository;
        this.contactDetailRepository = contactDetailRepository;
        this.voucherProductRepository = voucherProductRepository;
        this.productDetailRepository = productDetailRepository;
    }

    @Override
    public VoucherDto createVoucher(VoucherRequest voucherRequest) {
        VoucherDto voucherDto = voucherRequest.getVoucherDto();
        VoucherType voucherType = voucherTypeRepository.findById(voucherDto.getVoucherTypeID())
                                                        .orElseThrow(() -> new RuntimeException("VoucherType not found"));
        Voucher voucher = VoucherMapper.mapToVoucher(voucherDto);
        Voucher savedVoucher = voucherRepository.save(voucher);
        List<Long> productIDs = voucherRequest.getProductIDs();
        if(productIDs != null || !productIDs.isEmpty()) {
            productIDs.forEach(productID -> {
                String productDetailSize = voucherRequest.getSize();
                Long productDetailID = productDetailRepository.getProductDetailIdByProductIDAndSize(productID, productDetailSize);
                VoucherProduct voucherProduct = new VoucherProduct();
                voucherProduct.setVoucherID(savedVoucher.getId());
                voucherProduct.setProductDetailID(productDetailID);
                voucherProduct.setProductID(productID);
                voucherProductRepository.save(voucherProduct);
            });
        }
        return VoucherMapper.mapToVoucherDto(savedVoucher, voucherType);
    }

    @Override
    public void deleteVoucher(Long id) {
        Voucher voucher = voucherRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));
        voucherRepository.deleteById(id);

        List<VoucherProduct> voucherProducts = voucherProductRepository.findByVoucherID(id);
        voucherProductRepository.deleteAll(voucherProducts);

    }

    @Override
    public Page<VoucherDto> getAllVouchers(String name, int status, Date applyFrom, Date applyTo, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDayOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime lastDayOfMonth = now.withDayOfMonth(YearMonth.from(now).lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);

        LocalDateTime applyFromConverted = applyFrom != null ?
                dateTimeConverter.convertToDateViaInstant(applyFrom) : firstDayOfMonth;

        LocalDateTime applyToConverted = applyTo != null ?
                dateTimeConverter.convertToDateViaInstant(applyTo) : lastDayOfMonth;

        Page<Voucher> vouchers = voucherRepository.getAllVouchers(name, status, applyFromConverted, applyToConverted, pageable);


        return vouchers.map(voucher -> {
            VoucherType voucherType = voucherTypeRepository
                    .findById(voucher.getVoucherTypeID())
                    .orElseThrow(() -> new RuntimeException("VoucherType not found"));
            VoucherDto voucherDto = VoucherMapper.mapToVoucherDto(voucher, voucherType);
            List<VoucherProduct> voucherProducts = voucherProductRepository.findByVoucherID(voucher.getId());
            if (!voucherProducts.isEmpty()) {
                List<Long> productIDs = new ArrayList<>(); // Initialize the list
                String size = null;

                for (VoucherProduct voucherProduct : voucherProducts) {
                    Long productDetailID = voucherProduct.getProductDetailID();
                    // Retrieve size for the first product detail
                    if (size == null) {
                        size = productDetailRepository.getSizeByProductDetailID(productDetailID);
                    }
                    // Retrieve product ID
                    productIDs.add(voucherProduct.getProductID());
                }
                voucherDto.setProductIDs(productIDs);
                voucherDto.setSize(size);
            }
            return voucherDto;
        });
    }


    @Override
    @Transactional
    public VoucherDto updateVoucher(Long id, VoucherRequest voucherRequest) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));

        VoucherDto voucherDto = voucherRequest.getVoucherDto();

        VoucherType voucherType = voucherTypeRepository.findById(voucherDto.getVoucherTypeID())
                .orElseThrow(() -> new RuntimeException("VoucherType not found"));

        // Cập nhật thông tin voucher
        voucher.setName(voucherDto.getName());
        voucher.setCode(voucherDto.getCode());
        voucher.setVoucherTypeID(voucherDto.getVoucherTypeID());
        voucher.setDescription(voucherDto.getDescription());
        voucher.setImage(voucherDto.getImage());
        voucher.setDiscountValue(voucherDto.getDiscountValue());
        voucher.setMinimumOrderValue(voucherDto.getMinimumOrderValue());
        voucher.setMinimumItems(voucherDto.getMinimumItems());
        voucher.setMaxUses(voucherDto.getMaxUses());
        voucher.setCurrentUses(voucherDto.getCurrentUses());
        voucher.setStatus(voucherDto.getStatus());
        voucher.setErrorMessage(voucherDto.getErrorMessage());
        voucher.setComboPrice(voucherDto.getComboPrice());
        voucher.setFixedPrice(voucherDto.getFixedPrice());
        voucher.setBuy1Get1(voucherDto.getBuy1Get1());
        voucher.setFreeShip(voucherDto.getFreeShip());
        voucher.setProductType(voucherDto.getProductType());
        voucher.setDiscountMax(voucherDto.getDiscountMax());
        voucher.setComboQuantity(voucherDto.getComboQuantity());
        voucher.setApplyFrom(voucherDto.getApplyFrom());
        voucher.setApplyTo(voucherDto.getApplyTo());

        Voucher updatedVoucher = voucherRepository.save(voucher);

        // Xóa các VoucherProduct cũ
        voucherProductRepository.deleteByVoucherID(updatedVoucher.getId());

        // Tạo mới các VoucherProduct
        if (voucherRequest.getProductIDs() != null && !voucherRequest.getProductIDs().isEmpty()) {
            List<VoucherProduct> newVoucherProducts = new ArrayList<>();
            for (Long productID : voucherRequest.getProductIDs()) {
                VoucherProduct voucherProduct = new VoucherProduct();
                voucherProduct.setVoucherID(updatedVoucher.getId());
                voucherProduct.setProductID(productID);
                voucherProduct.setProductDetailID(productDetailRepository.getProductDetailIdByProductIDAndSize(productID, voucherRequest.getSize()));
                newVoucherProducts.add(voucherProduct);
            }

            voucherProductRepository.saveAll(newVoucherProducts);
        }

        // Tạo lại VoucherDto
        VoucherDto newVoucherDto = VoucherMapper.mapToVoucherDto(updatedVoucher, voucherType);
        newVoucherDto.setProductIDs(voucherRequest.getProductIDs());
        newVoucherDto.setSize(voucherRequest.getSize());

        return newVoucherDto;
    }

    @Override
    public List<VoucherDto> getVouchers() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDayOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime lastDayOfMonth = now.withDayOfMonth(YearMonth.from(now).lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);

        List<Voucher> vouchers = voucherRepository.getVouchers(firstDayOfMonth, lastDayOfMonth);

        return vouchers.stream().map(voucher -> {
            Optional<VoucherType> optionalVoucherType = voucherTypeRepository.findById(voucher.getVoucherTypeID());
            VoucherType voucherType = optionalVoucherType.orElse(null);
            assert voucherType != null;
            VoucherDto voucherDto = VoucherMapper.mapToVoucherDto(voucher, voucherType);
            List<VoucherProduct> voucherProducts = voucherProductRepository.findByVoucherID(voucher.getId());
            if (!voucherProducts.isEmpty()) {
                List<Long> productIDs = new ArrayList<>();
                String size = null;

                for (VoucherProduct voucherProduct : voucherProducts) {
                    Long productDetailID = voucherProduct.getProductDetailID();
                    // Retrieve size for the first product detail
                    if (size == null) {
                        size = productDetailRepository.getSizeByProductDetailID(productDetailID);
                    }
                    // Retrieve product ID from the productDetailRepository
                    productIDs.add(voucherProduct.getProductID());
                }
                voucherDto.setProductIDs(productIDs);
                voucherDto.setSize(size);
            }
            return voucherDto;
        }).collect(Collectors.toList());
    }

    public List<VoucherDto> getVouchersByUserId(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDayOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime lastDayOfMonth = now.withDayOfMonth(YearMonth.from(now).lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
        List<Voucher> vouchers = voucherRepository.getVouchers(firstDayOfMonth, lastDayOfMonth);

        // Lấy các voucher mà người dùng có quyền sử dụng từ bảng UserVoucher
        List<Voucher> userVouchers = userVoucherRepository.findByUserID(userId);

        // Kết hợp voucher từ cả hai nguồn
        List<Voucher> allVouchers = new ArrayList<>();
        allVouchers.addAll(vouchers);
        allVouchers.addAll(userVouchers);

        // Loại bỏ các voucher trùng lặp (nếu có)
        List<Voucher> uniqueVouchers = allVouchers.stream()
                .distinct()
                .toList();

        return uniqueVouchers.stream().filter(voucher -> {
            // Kiểm tra nếu người dùng đã sử dụng voucher này
            boolean alreadyUsed = billRepository.existsByUserIDAndVoucherID(userId, voucher.getId());

            // Kiểm tra các điều kiện khác như hiệu lực của voucher
            boolean valid = now.isBefore(voucher.getApplyTo()) && now.isAfter(voucher.getApplyFrom());
            log.info("voucher valid: {}", valid);

            // Chỉ trả về các voucher chưa sử dụng và còn hiệu lực
            return !alreadyUsed && valid;
        }).map(voucher -> {
            VoucherType voucherType = voucherTypeRepository
                    .findById(voucher.getVoucherTypeID())
                    .orElseThrow(() -> new RuntimeException("VoucherType not found"));
            assert voucherType != null;
            VoucherDto voucherDto = VoucherMapper.mapToVoucherDto(voucher, voucherType);
            List<VoucherProduct> voucherProducts = voucherProductRepository.findByVoucherID(voucher.getId());
            if (!voucherProducts.isEmpty()) {
                List<Long> productIDs = new ArrayList<>();
                String size = null;

                for (VoucherProduct voucherProduct : voucherProducts) {
                    Long productDetailID = voucherProduct.getProductDetailID();
                    // Retrieve size for the first product detail
                    if (size == null) {
                        size = productDetailRepository.getSizeByProductDetailID(productDetailID);
                    }
                    // Retrieve product ID from the productDetailRepository
                    productIDs.add(voucherProduct.getProductID());
                }
                voucherDto.setProductIDs(productIDs);
                voucherDto.setSize(size);
            }
            return voucherDto;
        }).collect(Collectors.toList());
    }

    @Override
    public VoucherType createVoucherType(VoucherType voucherType) {
        return voucherTypeRepository.save(voucherType);
    }

    @Override
    public List<VoucherType> getVoucherTypes() {
        return voucherTypeRepository.findAll();
    }

    @Override
    public VoucherDto getVoucherById(Long id) {
        Voucher voucher = voucherRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));
        VoucherType voucherType = voucherTypeRepository
                .findById(voucher.getVoucherTypeID())
                .orElseThrow(() -> new RuntimeException("VoucherType not found"));
        VoucherDto voucherDto = VoucherMapper.mapToVoucherDto(voucher, voucherType);
        List<VoucherProduct> voucherProducts = voucherProductRepository.findByVoucherID(voucher.getId());
        if (!voucherProducts.isEmpty()) {
            List<Long> productIDs = new ArrayList<>(); // Initialize the list
            String size = null;

            for (VoucherProduct voucherProduct : voucherProducts) {
                Long productDetailID = voucherProduct.getProductDetailID();
                // Retrieve size for the first product detail
                if (size == null) {
                    size = productDetailRepository.getSizeByProductDetailID(productDetailID);
                }
                // Retrieve product ID
                productIDs.add(voucherProduct.getProductID());
            }
            voucherDto.setProductIDs(productIDs);
            voucherDto.setSize(size);
        }
        return voucherDto;
    }

    @Override
    public VoucherDto getVoucherByCode(String code) {
        Voucher voucher = voucherRepository.findByCode(code);
        VoucherType voucherType = voucherTypeRepository
                .findById(voucher.getVoucherTypeID())
                .orElseThrow(() -> new RuntimeException("VoucherType not found"));
        log.info("voucher: {}", voucher);

        return VoucherMapper.mapToVoucherDto(voucher, voucherType);
    }

    @Override
    public Boolean getVoucherByPhoneNumber(Long voucherID, String phoneNumber) {
        ContactDetails contactDetails = contactDetailRepository.findFirstByPhoneNumber(phoneNumber);
        if(contactDetails != null) {
            if(contactDetails.getOwnerType() == OwnerType.USER) {
                return billRepository.existsByUserIDAndVoucherID(contactDetails.getOwnerID(), voucherID);
            }
            else {
                return billRepository.existsByCustomerIDAndVoucherID(contactDetails.getOwnerID(), voucherID);
            }
        }
        return false;
    }

    @Override
    public void assignVouchersToUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Xác định cấp độ thành viên của người dùng
        String membershipLevel = user.getMembershipLevel();

        // Tìm các voucher phù hợp với cấp độ thành viên
        List<Voucher> vouchers = voucherRepository.findByVoucherTypeID(
                getVoucherTypeIdForMembershipLevel(membershipLevel)
        );

        // Cấp phát các voucher cho người dùng
        for (Voucher voucher : vouchers) {
            UserVoucher userVoucher = userVoucherRepository.findByUserIDAndAndVoucherID(userId, voucher.getId());
            if (userVoucher == null) {
                userVoucher = new UserVoucher();
                userVoucher.setUserID(userId);
                userVoucher.setVoucherID(voucher.getId());
                userVoucher.setCreateTime(LocalDateTime.now());
                userVoucherRepository.save(userVoucher);
            }
        }
    }

    private Long getVoucherTypeIdForMembershipLevel(String membershipLevel) {
        VoucherType voucherType = voucherTypeRepository.findByType(membershipLevel);
        return voucherType.getId();
    }
}
