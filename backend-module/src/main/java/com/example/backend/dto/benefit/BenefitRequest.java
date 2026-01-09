package com.example.backend.dto.benefit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para criar ou atualizar um benefício")
public class BenefitRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Por favor, informe o name do benefício")
    @Size(min = 3, max = 100, message = "O name precisa ter entre 3 e 100 caracteres")
    @Schema(description = "Nome do benefício", example = "Vale Alimentação", required = true, minLength = 3, maxLength = 100)
    private String name;

    @Size(max = 500, message = "A descrição não pode ultrapassar 500 caracteres")
    @Schema(description = "Descrição detalhada do benefício", example = "Benefício para compras em supermercados", maxLength = 500)
    private String description;

    @NotNull(message = "Informe o value do benefício")
    @DecimalMin(value = "0.0", inclusive = true, message = "O value deve ser zero ou positivo")
    @Schema(description = "Valor monetário do benefício", example = "500.00", required = true, minimum = "0")
    private BigDecimal value;

    @NotNull(message = "Informe se o benefício está active ou inativo")
    @Schema(description = "Indica se o benefício está active", example = "true", required = true)
    private Boolean active;

    @Override
    public String toString() {
        return "BenefitRequest{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", active=" + active +
                '}';
    }
}
