package com.example.ejb;

import com.example.ejb.entity.Benefit;
import com.example.ejb.exception.*;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BenefitEjbService {

    private static final Logger logger = LoggerFactory.getLogger(BenefitEjbService.class);

    @PersistenceContext(unitName = "benefitPU")
    private EntityManager entityManager;

    public void transfer(Long fromBenefitId, Long toBenefitId, BigDecimal amount)
            throws BenefitException {

        logger.info(
                "Starting transfer: fromBenefitId={} toBenefitId={} amount={}",
                fromBenefitId, toBenefitId, amount
        );

        validateTransferInputs(fromBenefitId, toBenefitId, amount);

        var firstLockId = Math.min(fromBenefitId, toBenefitId);
        var secondLockId = Math.max(fromBenefitId, toBenefitId);

        logger.debug(
                "Acquiring locks in order: first={}, second={}",
                firstLockId, secondLockId
        );

        var first =
                entityManager.find(Benefit.class, firstLockId, LockModeType.PESSIMISTIC_WRITE);

        var second =
                entityManager.find(Benefit.class, secondLockId, LockModeType.PESSIMISTIC_WRITE);

        if (first == null) {
            logger.warn("Benefit not found: {}", firstLockId);
            throw new BenefitNotFoundException(firstLockId);
        }

        if (second == null) {
            logger.warn("Benefit not found: {}", secondLockId);
            throw new BenefitNotFoundException(secondLockId);
        }

        var source =
                fromBenefitId.equals(firstLockId) ? first : second;

        var destination =
                fromBenefitId.equals(firstLockId) ? second : first;

        logger.debug("Source benefit: {}", source);
        logger.debug("Destination benefit: {}", destination);

        validateActiveBenefit(source, "source", fromBenefitId);
        validateActiveBenefit(destination, "destination", toBenefitId);
        validateSufficientBalance(source, amount);

        var previousSourceBalance = source.getValue();
        var previousDestinationBalance = destination.getValue();

        var newSourceBalance =
                previousSourceBalance.subtract(amount);

        var newDestinationBalance =
                previousDestinationBalance.add(amount);

        source.setValue(newSourceBalance);
        destination.setValue(newDestinationBalance);

        entityManager.merge(source);
        entityManager.merge(destination);

        logger.info(
                "Transfer completed successfully. Source: {} -> {}, Destination: {} -> {}",
                previousSourceBalance,
                newSourceBalance,
                previousDestinationBalance,
                newDestinationBalance
        );
    }

    private void validateTransferInputs(
            Long fromBenefitId,
            Long toBenefitId,
            BigDecimal amount
    ) {

        if (fromBenefitId == null) {
            throw new IllegalArgumentException(
                    "Source benefit ID must not be null"
            );
        }

        if (toBenefitId == null) {
            throw new IllegalArgumentException(
                    "Destination benefit ID must not be null"
            );
        }

        if (amount == null) {
            throw new IllegalArgumentException(
                    "Transfer amount must not be null"
            );
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Transfer amount must be greater than zero: " + amount
            );
        }

        if (fromBenefitId.equals(toBenefitId)) {
            throw new IllegalArgumentException(
                    "Source and destination benefits must be different: " + fromBenefitId
            );
        }
    }

    private void validateActiveBenefit(
            Benefit benefit,
            String role,
            Long benefitId
    ) throws InactiveBenefitException {

        if (!benefit.isActive()) {
            logger.warn(
                    "Benefit is inactive. role={}, id={}",
                    role, benefitId
            );
            throw new InactiveBenefitException(benefitId, role);
        }
    }

    private void validateSufficientBalance(
            Benefit source,
            BigDecimal amount
    ) throws InsufficientBalanceException {

        if (source.getValue().compareTo(amount) < 0) {
            logger.warn(
                    "Insufficient balance. Available={}, Requested={}",
                    source.getValue(), amount
            );
            throw new InsufficientBalanceException(
                    source.getValue(),
                    amount
            );
        }
    }
}
