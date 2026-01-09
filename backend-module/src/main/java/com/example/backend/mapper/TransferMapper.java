package com.example.backend.mapper;

import com.example.backend.dto.transaction.TransactionalRequest;
import com.example.backend.dto.transfer.TransferRequest;
import com.example.backend.dto.transfer.TransferResult;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransferMapper {
    public TransactionalRequest toTransaction(
            TransferRequest transferRequest,
            TransferResult transferResult) {

        return new TransactionalRequest(
                transferRequest.getBenefitOriginId(),
                transferRequest.getBenefitDestinationId(),
                transferRequest.getValue(),
                transferResult.getPreviousBalanceSource(),
                transferResult.getOrigin().getValue(),
                transferResult.getPreviousBalanceDestination(),
                transferResult.getDestination().getValue(),
                LocalDateTime.now()
        );
    }
}
