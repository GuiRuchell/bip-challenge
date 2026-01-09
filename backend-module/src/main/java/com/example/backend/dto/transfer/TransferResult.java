package com.example.backend.dto.transfer;

import com.example.backend.entity.BenefitEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TransferResult {
    private BenefitEntity origin;
    private BenefitEntity destination;
    private BigDecimal previousBalanceSource;
    private BigDecimal previousBalanceDestination;
}
