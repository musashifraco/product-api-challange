package com.example.product_management_api.service;

import com.example.product_management_api.entity.Product;
import com.example.product_management_api.entity.dto.ProductDTO;
import com.example.product_management_api.exception.ProductDataAlreadyExistsException;
import com.example.product_management_api.exception.ProductNotFoundException;
import com.example.product_management_api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private List<Product> productList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productList = new ArrayList<>();
        productList.add(new Product("Product 1", "0000", 100.0, ""));
    }

    @Test
    void findAll_shouldReturnAllProducts() {
        when(productRepository.findAll()).thenReturn(productList);

        List<Product> result = productService.findAll();

        assertEquals(productList.size(), result.size());
        verify(productRepository).findAll();
    }

    @Test
    void findById_whenProductExists_shouldReturnProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(productList.get(0)));

        Optional<Product> result = productService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Product 1", result.get().getDescription());
    }

    @Test
    void findById_whenProductNotExists_shouldThrowProductNotFoundException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findById(1L));
    }

    @Test
    void save_whenProductDataDoesNotExist_shouldSaveProduct() {
        ProductDTO productDTO = new ProductDTO("New Product", "150.0", 100.1, "");
        when(productRepository.findByDescriptionOrBarcode(any(), any())).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(new Product("New Product", "150.0", 100.1, ""));

        Product result = productService.save(productDTO);

        assertEquals("New Product", result.getDescription());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void save_whenProductDataAlreadyExists_shouldThrowProductDataAlreadyExistsException() {
        ProductDTO productDTO = new ProductDTO("Existing Product", "150.0", 100.1, "");
        when(productRepository.findByDescriptionOrBarcode(any(), any())).thenReturn(Optional.of(new Product()));

        assertThrows(ProductDataAlreadyExistsException.class, () -> productService.save(productDTO));
    }

    @Test
    void updateProduct_whenProductExists_shouldUpdateProduct() {
        ProductDTO productDTO = new ProductDTO("Updated Product", "120.0", 100.2, "kg");
        when(productRepository.findById(1L)).thenReturn(Optional.of(productList.get(0)));
        when(productRepository.save(any(Product.class))).thenReturn(new Product("Updated Product", "000000000", 120.0, "kg"));

        Product result = productService.updateProduct(1L, productDTO);

        assertEquals("Updated Product", result.getDescription());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_whenProductNotExists_shouldThrowProductNotFoundException() {
        ProductDTO productDTO = new ProductDTO("Updated Product", "120.0", 100.2, "kg");
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, productDTO));
    }

    @Test
    void deleteProduct_whenProductExists_shouldDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(productList.get(0)));
        doNothing().when(productRepository).delete(any(Product.class));

        assertDoesNotThrow(() -> productService.deleteProduct(1L));
        verify(productRepository).delete(any(Product.class));
    }

    @Test
    void deleteProduct_whenProductNotExists_shouldThrowProductNotFoundException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
    }
}
