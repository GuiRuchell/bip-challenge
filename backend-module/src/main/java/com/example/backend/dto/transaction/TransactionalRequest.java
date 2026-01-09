package com.example.backend.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Detalhes da transação de transferência")
public class TransactionalRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID do benefício que será debitado na transferência", example = "1")
    private Long benefitOriginId;

    @Schema(description = "ID do benefício que receberá o value da transferência", example = "2")
    private Long benefitDestinationId;

    @Schema(description = "Valor total que será transferido entre os benefícios", example = "300.00")
    private BigDecimal value;

    @Schema(description = "Saldo do benefício de origem antes da transferência", example = "1000.00")
    private BigDecimal previousBalanceSource;

    @Schema(description = "Saldo do benefício de origem após a transferência", example = "700.00")
    private BigDecimal newBalanceOrigin;

    @Schema(description = "Saldo do benefício de destino antes da transferência", example = "500.00")
    private BigDecimal previousBalanceDestination;

    @Schema(description = "Saldo do benefício de destino após a transferência", example = "800.00")
    private BigDecimal newDestinationBalance;

    @Schema(description = "Data e hora em que a transferência foi realizada", example = "2025-11-08T10:30:00")
    private LocalDateTime timestamp;
}
