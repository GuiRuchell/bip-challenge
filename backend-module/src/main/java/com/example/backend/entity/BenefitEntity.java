package com.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class BenefitEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(name = "NOME", nullable = false, length = 100)
    private String name;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    @Column(name = "DESCRICAO", length = 500)
    private String description;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.0", inclusive = true, message = "Valor deve ser maior ou igual a 0")
    @Column(name = "VALOR", nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @Column(name = "ATIVO", nullable = false)
    private Boolean active = true;

    @Version
    @Column(name = "VERSION")
    private Long version;

    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    public BenefitEntity(String name, String description, BigDecimal value) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.active = true;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.active == null) {
            this.active = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Beneficio[" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", active=" + active +
                ", version=" + version +
                ']';
    }
}
