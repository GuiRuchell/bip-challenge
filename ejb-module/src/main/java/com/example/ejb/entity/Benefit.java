package com.example.ejb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "BENEFIT")
public class Benefit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @Column(name = "DESCRIPTION", length = 255)
    @Size(max = 255, message = "Description must be at most 255 characters")
    private String description;

    @Column(name = "VALUE", nullable = false, precision = 15, scale = 2)
    @NotNull(message = "Value is required")
    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "Value must not be negative"
    )
    private BigDecimal value;

    @Column(name = "ACTIVE", nullable = false)
    @NotNull(message = "Active flag is required")
    private Boolean active = true;

    @Version
    @Column(name = "VERSION")
    private Long version;

    public Benefit() {
        this.active = true;
    }

    public Benefit(String name, String description, BigDecimal value) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.active = true;
    }

    public Benefit(
            String name,
            String description,
            BigDecimal value,
            Boolean active
    ) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.active = active != null ? active : true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Boolean isActive() {
        return active;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @PrePersist
    @PreUpdate
    private void validateValue() {
        if (value != null && value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException(
                    "Value must not be negative: " + value
            );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Benefit benefit = (Benefit) o;
        return id != null && id.equals(benefit.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return String.format(
                "Benefit[id=%d, name='%s', value=%s, active=%s, version=%d]",
                id, name, value, active, version
        );
    }
}
