package com.example.product_management_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "products", uniqueConstraints = {
        @UniqueConstraint(columnNames = "description"),
        @UniqueConstraint(columnNames = "barcode")
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 255, message = "A descrição não pode ter mais de 255 caracteres")
    @Column(name = "description", nullable = false, unique = true)
    private String description;

    @NotBlank(message = "O código de barra é obrigatório")
    @Column(name = "barcode", nullable = false, unique = true)
    private String barcode;

    @DecimalMin(value = "0.01", message = "O preço unitário deve ser maior que zero")
    @NotNull(message = "O preço unitário é obrigatório")
    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @NotBlank(message = "A unidade de medida é obrigatória")
    @Column(name = "unit_of_measure", nullable = false)
    private String unitOfMeasure;

    @Column(name = "registration_date", updatable = false)
    private LocalDateTime registrationDate;

    public Product() {
    }

    public Product(String description, String barcode, Double unitPrice, String unitOfMeasure) {
        this.description = description;
        this.barcode = barcode;
        this.unitPrice = unitPrice;
        this.unitOfMeasure = unitOfMeasure;
    }

    @PrePersist
    protected void onCreate() {
        this.registrationDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
}
