package com.example.thecoffeehouse.service.impl;

import com.example.thecoffeehouse.DateTimeConverter;
import com.example.thecoffeehouse.dto.VoucherDto;
import com.example.thecoffeehouse.entity.Voucher;
import com.example.thecoffeehouse.entity.mapper.VoucherMapper;
import com.example.thecoffeehouse.repository.VoucherRepository;
import com.example.thecoffeehouse.service.VoucherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Date;

@Service
public class VoucherServiceImpl implements VoucherService {
    private static final Logger log = LoggerFactory.getLogger(VoucherServiceImpl.class);
    private final VoucherRepository voucherRepository;
    private final DateTimeConverter dateTimeConverter;

    public VoucherServiceImpl(VoucherRepository voucherRepository, DateTimeConverter dateTimeConverter) {
        this.voucherRepository = voucherRepository;
        this.dateTimeConverter = dateTimeConverter;
    }

    @Override
    public VoucherDto createVoucher(VoucherDto voucherDto) {
        Voucher voucher = VoucherMapper.mapToVoucher(voucherDto);
        Voucher savedVoucher = voucherRepository.save(voucher);
        return VoucherMapper.mapToVoucherDto(savedVoucher);
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

        log.info("applyfrom: {}, applyto: {}", applyFromConverted, applyToConverted);
        Page<Voucher> vouchers = voucherRepository.getAllVouchers(name, status, applyFromConverted, applyToConverted, pageable);
        return vouchers.map(VoucherMapper::mapToVoucherDto);
    }

    @Override
    public VoucherDto updateVoucher(Long id, VoucherDto voucherDto) {
        Voucher voucher = voucherRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));

        voucher.setName(voucherDto.getName());
        voucher.setValue(voucherDto.getValue());
        voucher.setStatus(voucherDto.getStatus());
        voucher.setImage(voucherDto.getImage());
        voucher.setApplyFrom(voucherDto.getApplyFrom());
        voucher.setApplyTo(voucherDto.getApplyTo());

        Voucher updatedVoucher = voucherRepository.save(voucher);
        return VoucherMapper.mapToVoucherDto(updatedVoucher);
    }
}
