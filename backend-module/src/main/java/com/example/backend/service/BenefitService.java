package com.example.backend.service;

import com.example.backend.dto.benefit.BenefitRequest;
import com.example.backend.dto.benefit.BenefitResponse;
import com.example.backend.dto.transfer.TransferRequest;
import com.example.backend.dto.transfer.TransferResponse;
import com.example.backend.dto.transfer.TransferResult;
import com.example.backend.entity.BenefitEntity;
import com.example.backend.exception.BenefitConflictException;
import com.example.backend.exception.BenefitNotFoundException;
import com.example.backend.mapper.BenefitMapper;
import com.example.backend.mapper.TransferMapper;
import com.example.backend.repository.BenefitRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.AllArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.backend.validation.BenefitValidator.validateBenefits;
import static com.example.backend.validation.TransferValidator.validateTransferencia;

@AllArgsConstructor
@Service
@Transactional
public class BenefitService {

    private final BenefitRepository benefitRepository;
    private final BenefitMapper benefitMapper;
    private TransferMapper transferMapper;

    @Transactional(readOnly = true)
    public List<BenefitResponse> findAll() {
        return benefitRepository.findAll()
                .stream()
                .map(benefitMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BenefitResponse> findAllAtivos() {
        return benefitRepository.findAllAtivos()
                .stream()
                .map(benefitMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BenefitResponse findById(Long id) {
        return benefitMapper.toResponse(getBenefitOrThrow(id));
    }

    public BenefitResponse create(BenefitRequest benefitRequest) {
        validateUniqueName(benefitRequest.getName());

        var entity = benefitMapper.toEntity(benefitRequest);

        return benefitMapper.toResponse(benefitRepository.save(entity));
    }

    public BenefitResponse update(Long id, BenefitRequest benefitRequest) {
        var entity = getBenefitOrThrow(id);

        validateUniqueName(benefitRequest.getName());

        benefitMapper.updateEntity(entity, benefitRequest);

        return benefitMapper.toResponse(benefitRepository.save(entity));
    }

    public void delete(Long id) {
        var entity = getBenefitOrThrow(id);

        entity.setActive(false);
        benefitRepository.save(entity);
    }

    public TransferResponse transfer(TransferRequest transferRequest) {
        validateTransferencia(transferRequest);
        return execute(transferRequest);
    }


    @Retryable(
            value = {
                    OptimisticLockException.class,
                    ObjectOptimisticLockingFailureException.class
            },
            maxAttempts = 3,
            backoff = @Backoff(delay = 100, multiplier = 2)
    )
    public TransferResponse execute(TransferRequest transferRequest) {

        var result =
                executeTransfer(transferRequest);

        var transaction =
                transferMapper.toTransaction(transferRequest, result);

        return new TransferResponse(
                true,
                "Transferência realizada com success",
                transaction
        );
    }

    @Recover
    public TransferResponse recover(
            Exception e,
            TransferRequest transferRequest) {

        throw new BenefitConflictException(
                "Transferência falhou devido a conflito de concorrência"
        );
    }

    @Transactional
    public TransferResult executeTransfer(TransferRequest transferRequest) {

        var origin = findBenefitOrThrow(transferRequest.getBenefitOriginId(), "origin");
        var destination = findBenefitOrThrow(transferRequest.getBenefitDestinationId(), "destination");

        validateBenefits(origin, destination, transferRequest.getValue());

        var previousBalanceSource = origin.getValue();
        var previousBalanceDestination = destination.getValue();

        origin.setValue(origin.getValue().subtract(transferRequest.getValue()));
        destination.setValue(destination.getValue().add(transferRequest.getValue()));

        benefitRepository.save(origin);
        benefitRepository.save(destination);

        return new TransferResult(
                origin,
                destination,
                previousBalanceSource,
                previousBalanceDestination
        );
    }

    private BenefitEntity findBenefitOrThrow(Long benefitId, String type) {
        return benefitRepository.findById(benefitId)
                .orElseThrow(() -> new BenefitNotFoundException(
                        String.format("Benefício de %s não encontrado: %d", type, benefitId)));
    }

    private BenefitEntity getBenefitOrThrow(Long id) {
        return benefitRepository.findById(id)
                .orElseThrow(() -> new BenefitNotFoundException("Beneficio não encontrado"));
    }

    private void validateUniqueName(String name) {
        if (benefitRepository.existsByName(name)) {
            throw new BenefitConflictException(
                    "Já existe um benefício com o name informado: " + name
            );
        }
    }
}
