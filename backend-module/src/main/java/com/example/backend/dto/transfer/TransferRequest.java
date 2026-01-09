package com.example.backend.dto.transfer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Requisição para transferir value entre benefícios")
public class TransferRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Por favor, informe o ID do benefício de origem")
    @Schema(description = "ID do benefício que será debitado na transferência", example = "1", required = true)
    private Long benefitOriginId;

    @NotNull(message = "Por favor, informe o ID do benefício de destino")
    @Schema(description = "ID do benefício que receberá o value da transferência", example = "2", required = true)
    private Long benefitDestinationId;

    @NotNull(message = "Informe o value que será transferido")
    @DecimalMin(value = "0.01", inclusive = true, message = "O value deve ser maior que zero")
    @Schema(description = "Quantia a ser transferida entre os benefícios", example = "300.00", required = true, minimum = "0.01")
    private BigDecimal value;


    @Override
    public String toString() {
        return "TransferRequest{" +
                "benefitOriginId=" + benefitOriginId +
                ", benefitDestinationId=" + benefitDestinationId +
                ", value=" + value +
                '}';
    }
}
