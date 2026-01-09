package com.example.backend.dto.benefit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Representação completa de um benefício")
public class BenefitResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID único do benefício", example = "1")
    private Long id;

    @Schema(description = "Nome do benefício", example = "Vale Refeição", required = true)
    private String name;

    @Schema(description = "Descrição detalhada do benefício", example = "Benefício para alimentação")
    private String description;

    @Schema(description = "Valor monetário do benefício", example = "1000.00", required = true)
    private BigDecimal value;

    @Schema(description = "Indica se o benefício está active", example = "true", required = true)
    private Boolean active;

    @Schema(description = "Versão para controle de concorrência otimista", example = "0")
    private Long version;

    @Schema(description = "Data e hora de criação do benefício")
    private LocalDateTime createdAt;

    @Schema(description = "Data e hora da última atualização do benefício")
    private LocalDateTime updatedAt;

    public BenefitResponse(Long id, String name, String description, BigDecimal value, Boolean active, Long version) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.value = value;
        this.active = active;
        this.version = version;
    }

    @Override
    public String toString() {
        return "BeneficioDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", active=" + active +
                ", version=" + version +
                '}';
    }
}
