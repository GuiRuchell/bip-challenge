package com.example.backend.service;

import com.example.backend.dto.benefit.BenefitRequest;
import com.example.backend.dto.benefit.BenefitResponse;
import com.example.backend.dto.transfer.TransferRequest;
import com.example.backend.dto.transfer.TransferResult;
import com.example.backend.entity.BenefitEntity;
import com.example.backend.exception.BenefitConflictException;
import com.example.backend.exception.BenefitNotFoundException;
import com.example.backend.mapper.BenefitMapper;
import com.example.backend.mapper.TransferMapper;
import com.example.backend.repository.BenefitRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class BenefitServiceTest {

    @Mock
    private BenefitRepository benefitRepository;

    @Mock
    private BenefitMapper benefitMapper;

    @Mock
    private TransferMapper transferMapper;

    @InjectMocks
    private BenefitService benefitService;

    @Test
    public void shouldReturnAllBenefits() {
        var entity = new BenefitEntity();
        var response = new BenefitResponse();

        when(benefitRepository.findAll()).thenReturn(List.of(entity));
        when(benefitMapper.toResponse(entity)).thenReturn(response);

        var result = benefitService.findAll();

        assertEquals(1, result.size());
        verify(benefitRepository).findAll();
    }

    @Test
    public void shouldFindBenefitById() {
        var entity = new BenefitEntity();
        var response = new BenefitResponse();

        when(benefitRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(benefitMapper.toResponse(entity)).thenReturn(response);

        var result = benefitService.findById(1L);

        assertNotNull(result);
    }

    @Test
    public void shouldThrowExceptionWhenBenefitNotFound() {
        when(benefitRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BenefitNotFoundException.class,
                () -> benefitService.findById(1L));
    }

    @Test
    public void shouldCreateBenefit() {
        var request = new BenefitRequest();
        request.setName("Vale Transporte");

        var entity = new BenefitEntity();
        var response = new BenefitResponse();

        when(benefitRepository.existsByName("Vale Transporte")).thenReturn(false);
        when(benefitMapper.toEntity(request)).thenReturn(entity);
        when(benefitRepository.save(entity)).thenReturn(entity);
        when(benefitMapper.toResponse(entity)).thenReturn(response);

        var result = benefitService.create(request);

        assertNotNull(result);
        verify(benefitRepository).save(entity);
    }

    @Test
    public void shouldThrowExceptionWhenCreatingDuplicateName() {
        var request = new BenefitRequest();
        request.setName("Duplicado");

        when(benefitRepository.existsByName("Duplicado")).thenReturn(true);

        assertThrows(BenefitConflictException.class,
                () -> benefitService.create(request));
    }

    @Test
    public void shouldSoftDeleteBenefit() {
        var entity = new BenefitEntity();
        entity.setActive(true);

        when(benefitRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(benefitRepository.save(entity)).thenReturn(entity);

        benefitService.delete(1L);

        assertFalse(entity.getActive());
        verify(benefitRepository).save(entity);
    }

    @Test
    public void shouldExecuteTransferSuccessfully() {
        var request = new TransferRequest();
        request.setBenefitOriginId(1L);
        request.setBenefitDestinationId(2L);
        request.setValue(BigDecimal.TEN);

        var origin = new BenefitEntity();
        origin.setValue(BigDecimal.valueOf(100));

        var destination = new BenefitEntity();
        destination.setValue(BigDecimal.valueOf(50));

        when(benefitRepository.findById(1L)).thenReturn(Optional.of(origin));
        when(benefitRepository.findById(2L)).thenReturn(Optional.of(destination));
        when(benefitRepository.save(any(BenefitEntity.class))).thenAnswer(i -> i.getArgument(0));

        benefitService.executeTransfer(request);

        assertEquals(BigDecimal.valueOf(90), origin.getValue());
        assertEquals(BigDecimal.valueOf(60), destination.getValue());
    }
}
