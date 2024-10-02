package com.example.product_management_api.controller;

import com.example.product_management_api.entity.Product;
import com.example.product_management_api.entity.dto.ProductDTO;
import com.example.product_management_api.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Mock
    private RedisTemplate<String, List<Product>> redisTemplate;

    @Mock
    private ValueOperations<String, List<Product>> valueOperations;

    private List<Product> productList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productList = new ArrayList<>();
        productList.add(new Product("Product 1", "0000", 100.0, ""));

        // Configurando o mock para o RedisTemplate
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void getAllProducts_shouldReturnProductsAndCacheThem() {
        when(productService.findAll()).thenReturn(productList);

        List<Product> result = productController.getAllProducts();

        assertEquals(productList.size(), result.size());
        verify(valueOperations).set("products", productList);
    }

    @Test
    void getProductById_whenProductInCache_shouldReturnProduct() {
        ProductDTO productDTO = new ProductDTO("Updated Product", "120.0", 100.2, "asdasd");
        when(productService.updateProduct(1L, productDTO)).thenReturn(new Product("Updated Product", "000000000", 2.3, "120.0"));

        ResponseEntity<Product> response = productController.updateProduct(1L, productDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Product", response.getBody().getDescription());
        verify(redisTemplate).delete("products"); // Corrigido aqui
    }

    @Test
    void getProductById_whenProductNotInCache_shouldReturnProductFromService() {
        when(valueOperations.get("products")).thenReturn(null);
        when(productService.findById(1L)).thenReturn(Optional.of(productList.get(0)));

        ResponseEntity<Product> response = productController.getProductById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product 1", response.getBody().getDescription());
        verify(valueOperations).set(any(), any());
    }

    @Test
    void createProduct_shouldCreateAndInvalidateCache() {
        ProductDTO productDTO = new ProductDTO("New Product", "150.0", 100.1, "");
        Product newProduct = new Product("New Product", "150.0", 100.1, "");
        when(productService.save(any(ProductDTO.class))).thenReturn(newProduct);

        ResponseEntity<Product> response = productController.createProduct(productDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("New Product", response.getBody().getDescription());
        verify(redisTemplate).delete("products"); // Corrigido aqui
    }

    @Test
    void updateProduct_shouldUpdateAndInvalidateCache() {
        ProductDTO productDTO = new ProductDTO("Updated Product", "120.0", 100.2, "asdasd");
        when(productService.updateProduct(1L, productDTO)).thenReturn(new Product("desssasdasd", "000000000", 2.3, "120.0"));

        ResponseEntity<Product> response = productController.updateProduct(1L, productDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("desssasdasd", response.getBody().getDescription());
        verify(redisTemplate).delete("products"); // Corrigido aqui
    }

    @Test
    void deleteProduct_shouldDeleteAndInvalidateCache() {
        doNothing().when(productService).deleteProduct(1L);

        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(redisTemplate).delete("products"); // Corrigido aqui
    }

    @Test
    void checkCache_shouldReturnCacheStatus() {
        when(redisTemplate.hasKey("products")).thenReturn(true);

        ResponseEntity<String> response = productController.checkCache();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cache está ativo e contém produtos.", response.getBody());
    }
}
