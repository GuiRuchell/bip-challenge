package com.example.backend.validation;

import com.example.backend.dto.transfer.TransferRequest;
import com.example.backend.exception.BenefitConflictException;

import java.math.BigDecimal;

public class TransferValidator {

    public static void validateTransferencia(TransferRequest transferRequest) {
        if (transferRequest.getBenefitOriginId().equals(transferRequest.getBenefitDestinationId())) {
            throw new BenefitConflictException("Origem e destino não podem ser iguais");
        }

        if (transferRequest.getValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BenefitConflictException("Valor deve ser maior que zero");
        }
    }

}
