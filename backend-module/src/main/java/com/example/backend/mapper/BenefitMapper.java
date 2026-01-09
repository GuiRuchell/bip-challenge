package com.example.backend.mapper;

import com.example.backend.dto.benefit.BenefitRequest;
import com.example.backend.dto.benefit.BenefitResponse;
import com.example.backend.entity.BenefitEntity;
import org.springframework.stereotype.Component;

@Component
public class BenefitMapper {

    public BenefitResponse toResponse(BenefitEntity benefitEntity) {
        if (benefitEntity == null) {
            return null;
        }

        var beneficioResponse = new BenefitResponse(
                benefitEntity.getId(),
                benefitEntity.getName(),
                benefitEntity.getDescription(),
                benefitEntity.getValue(),
                benefitEntity.getActive(),
                benefitEntity.getVersion()
        );
        beneficioResponse.setCreatedAt(benefitEntity.getCreatedAt());
        beneficioResponse.setUpdatedAt(benefitEntity.getUpdatedAt());
        return beneficioResponse;
    }

    public BenefitEntity toEntity(BenefitRequest benefitRequest) {
        if (benefitRequest == null) {
            return null;
        }

        var entity = new BenefitEntity();

        entity.setName(benefitRequest.getName());
        entity.setDescription(benefitRequest.getDescription());
        entity.setValue(benefitRequest.getValue());
        entity.setActive(benefitRequest.getActive() != null ? benefitRequest.getActive() : true);

        return entity;
    }

    public void updateEntity(BenefitEntity benefitEntity, BenefitRequest benefitRequest) {
        if (benefitEntity == null || benefitRequest == null) {
            return;
        }

        benefitEntity.setName(benefitRequest.getName());
        benefitEntity.setDescription(benefitRequest.getDescription());
        benefitEntity.setValue(benefitRequest.getValue());
        benefitEntity.setActive(benefitRequest.getActive());
    }
}
