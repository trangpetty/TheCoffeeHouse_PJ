package com.example.thecoffeehouse.service.impl;

import com.example.thecoffeehouse.Utils.DateTimeConverter;
import com.example.thecoffeehouse.dto.VoucherDto;
import com.example.thecoffeehouse.entity.voucher.Voucher;
import com.example.thecoffeehouse.entity.mapper.VoucherMapper;
import com.example.thecoffeehouse.entity.voucher.VoucherType;
import com.example.thecoffeehouse.repository.VoucherRepository;
import com.example.thecoffeehouse.repository.VoucherTypeRepository;
import com.example.thecoffeehouse.repository.bill.BillRepository;
import com.example.thecoffeehouse.service.VoucherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
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

    public VoucherServiceImpl(VoucherRepository voucherRepository, VoucherTypeRepository voucherTypeRepository, DateTimeConverter dateTimeConverter, BillRepository billRepository) {
        this.voucherRepository = voucherRepository;
        this.voucherTypeRepository = voucherTypeRepository;
        this.dateTimeConverter = dateTimeConverter;
        this.billRepository = billRepository;
    }

    @Override
    public VoucherDto createVoucher(VoucherDto voucherDto) {
        VoucherType voucherType = voucherTypeRepository.findById(voucherDto.getVoucherTypeID())
                                                        .orElseThrow(() -> new RuntimeException("VoucherType not found"));
        Voucher voucher = VoucherMapper.mapToVoucher(voucherDto);
        Voucher savedVoucher = voucherRepository.save(voucher);
        return VoucherMapper.mapToVoucherDto(savedVoucher, voucherType);
    }

    @Override
    public void deleteVoucher(Long id) {
        Voucher voucher = voucherRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));
        voucherRepository.deleteById(id);
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

        log.info("from: {}", applyFromConverted);
        log.info("to: {}", applyToConverted);

        Page<Voucher> vouchers = voucherRepository.getAllVouchers(name, status, applyFromConverted, applyToConverted, pageable);

        return vouchers.map(voucher -> {
            Optional<VoucherType> optionalVoucherType = voucherTypeRepository.findById(voucher.getVoucherTypeID());
            VoucherType voucherType = optionalVoucherType.orElseThrow(() -> new RuntimeException("VoucherType not found"));
            return VoucherMapper.mapToVoucherDto(voucher, voucherType);
        });
    }


    @Override
    public VoucherDto updateVoucher(Long id, VoucherDto voucherDto) {
        Voucher voucher = voucherRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));

        VoucherType voucherType = voucherTypeRepository.findById(voucherDto.getVoucherTypeID())
                .orElseThrow(() -> new RuntimeException("VoucherType not found"));

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
        voucher.setApplyFrom(voucherDto.getApplyFrom());
        voucher.setApplyTo(voucherDto.getApplyTo());

        Voucher updatedVoucher = voucherRepository.save(voucher);
        return VoucherMapper.mapToVoucherDto(updatedVoucher, voucherType);
    }

    @Override
    public List<VoucherDto> getVouchers() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDayOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime lastDayOfMonth = now.withDayOfMonth(YearMonth.from(now).lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);

        List<Voucher> vouchers = voucherRepository.getVouchers(firstDayOfMonth, lastDayOfMonth);

        return vouchers.stream().map(voucher -> {
            Optional<VoucherType> optionalVoucherType = voucherTypeRepository.findById(voucher.getVoucherTypeID());
            VoucherType voucherType = optionalVoucherType.orElse(null); // Return null if VoucherType is not found
            assert voucherType != null;
            return VoucherMapper.mapToVoucherDto(voucher, voucherType);
        }).collect(Collectors.toList());
    }

    public List<VoucherDto> getVouchers(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDayOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime lastDayOfMonth = now.withDayOfMonth(YearMonth.from(now).lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);

        List<Voucher> vouchers = voucherRepository.getVouchers(firstDayOfMonth, lastDayOfMonth);

        if (userId != null) {
            return vouchers.stream().filter(voucher -> {
                // Check if the user has already used this voucher
                boolean alreadyUsed = billRepository.existsByUserIDAndVoucherID(userId, voucher.getId());

                // Check other conditions like voucher validity
                boolean valid = now.isBefore(voucher.getApplyTo()) && now.isAfter(voucher.getApplyFrom());

                return !alreadyUsed && valid;
            }).map(voucher -> {
                Optional<VoucherType> optionalVoucherType = voucherTypeRepository.findById(voucher.getVoucherTypeID());
                VoucherType voucherType = optionalVoucherType.orElse(null); // Return null if VoucherType is not found
                assert voucherType != null;
                return VoucherMapper.mapToVoucherDto(voucher, voucherType);
            }).collect(Collectors.toList());
        } else {
            // Handle case when userId is null
            return vouchers.stream().map(voucher -> {
                Optional<VoucherType> optionalVoucherType = voucherTypeRepository.findById(voucher.getVoucherTypeID());
                VoucherType voucherType = optionalVoucherType.orElse(null); // Return null if VoucherType is not found
                assert voucherType != null;
                return VoucherMapper.mapToVoucherDto(voucher, voucherType);
            }).collect(Collectors.toList());
        }
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
        Optional<VoucherType> optionalVoucherType = voucherTypeRepository.findById(voucher.getVoucherTypeID());
        VoucherType voucherType = optionalVoucherType.orElseThrow(() -> new RuntimeException("VoucherType not found"));
        return VoucherMapper.mapToVoucherDto(voucher, voucherType);
    }
}
