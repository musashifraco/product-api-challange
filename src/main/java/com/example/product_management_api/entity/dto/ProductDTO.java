package com.example.product_management_api.entity.dto;

import com.example.product_management_api.entity.Product;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

public record ProductDTO(
                         @NotBlank String description,
                         @NotBlank String barcode,
                         @Positive Double unitPrice,
                         @NotBlank String unitOfMeasure) {
    public Product toProduct() {
        return new Product(
                description,
                barcode,
                unitPrice,
                unitOfMeasure
        );
    }

    @Override
    public @NotBlank String description() {
        return description;
    }

    @Override
    public @NotBlank String barcode() {
        return barcode;
    }

    @Override
    public @Positive Double unitPrice() {
        return unitPrice;
    }

    @Override
    public @NotBlank String unitOfMeasure() {
        return unitOfMeasure;
    }
}
