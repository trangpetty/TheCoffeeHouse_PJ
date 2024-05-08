package com.example.thecoffeehouse.service.impl;

import com.example.thecoffeehouse.DateTimeConverter;
import com.example.thecoffeehouse.dto.VoucherDto;
import com.example.thecoffeehouse.entity.Voucher;
import com.example.thecoffeehouse.entity.mapper.VoucherMapper;
import com.example.thecoffeehouse.repository.VoucherRepository;
import com.example.thecoffeehouse.service.VoucherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Date;
import java.util.UUID;

@Service
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;
    private final DateTimeConverter dateTimeConverter;

    public VoucherServiceImpl(VoucherRepository voucherRepository, DateTimeConverter dateTimeConverter) {
        this.voucherRepository = voucherRepository;
        this.dateTimeConverter = dateTimeConverter;
    }

    @Override
    public VoucherDto createVoucher(VoucherDto voucherDto) {
        voucherDto.setId(UUID.randomUUID().toString().split("-")[0]);
        Voucher voucher = VoucherMapper.mapToVoucher(voucherDto);
        Voucher savedVoucher = voucherRepository.save(voucher);
        return VoucherMapper.mapToVoucherDto(savedVoucher);
    }

    @Override
    public void deleteVoucher(String id) {
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

        Page<Voucher> vouchers = voucherRepository.getAllVouchers(name, status, applyFromConverted, applyToConverted, pageable);
        return vouchers.map(VoucherMapper::mapToVoucherDto);
    }

    @Override
    public VoucherDto updateVoucher(String id, VoucherDto voucherDto) {
        Voucher voucher = voucherRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));

        voucher.setTitle(voucherDto.getTitle());
        voucher.setValue(voucherDto.getValue());
        voucher.setStatus(voucherDto.getStatus());
        voucher.setImage(voucherDto.getImage());
        voucher.setDescription(voucherDto.getDescription());
        voucher.setCode(voucherDto.getCode());
        voucher.setApplyFrom(voucherDto.getApplyFrom());
        voucher.setApplyTo(voucherDto.getApplyTo());

        Voucher updatedVoucher = voucherRepository.save(voucher);
        return VoucherMapper.mapToVoucherDto(updatedVoucher);
    }
}
