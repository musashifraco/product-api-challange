package com.example.product_management_api.service;


import com.example.product_management_api.entity.Product;
import com.example.product_management_api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com o ID: " + id));

        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setBarcode(productDetails.getBarcode());
        existingProduct.setUnitPrice(productDetails.getUnitPrice());
        existingProduct.setUnitOfMeasure(productDetails.getUnitOfMeasure());
        existingProduct.setRegistrationDate(productDetails.getRegistrationDate());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com o ID: " + id));

        productRepository.delete(existingProduct);
    }
}
