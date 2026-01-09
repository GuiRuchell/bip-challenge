package com.example.backend.validation;

import com.example.backend.entity.BenefitEntity;
import com.example.backend.exception.BenefitConflictException;

import java.math.BigDecimal;

public class BenefitValidator {

    public static void validateBenefits(
            BenefitEntity origin,
            BenefitEntity destination,
            BigDecimal value) {

        if (!origin.getActive()) {
            throw new BenefitConflictException("Benefício de origin está inativo");
        }
        if (!destination.getActive()) {
            throw new BenefitConflictException("Benefício de destination está inativo");
        }
        if (origin.getValue().compareTo(value) < 0) {
            throw new BenefitConflictException(
                    String.format("Saldo insuficiente. Disponível: %.2f, Solicitado: %.2f",
                            origin.getValue(), value));
        }
    }
}
