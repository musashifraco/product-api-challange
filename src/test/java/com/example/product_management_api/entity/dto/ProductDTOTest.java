package com.example.product_management_api.entity.dto;

import com.example.product_management_api.entity.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductDTOTest {

    @Test
    void testToProduct() {
        ProductDTO productDTO = new ProductDTO("Product 1", "1234567890123", 10.0, "Unit");
        Product product = productDTO.toProduct();

        assertEquals("Product 1", product.getDescription());
        assertEquals("1234567890123", product.getBarcode());
        assertEquals(10.0, product.getUnitPrice());
        assertEquals("Unit", product.getUnitOfMeasure());
    }
}
