package com.example.product_management_api.service;


import com.example.product_management_api.entity.Product;
import com.example.product_management_api.entity.dto.ProductDTO;
import com.example.product_management_api.exception.ProductDataAlreadyExistsException;
import com.example.product_management_api.exception.ProductNotFoundException;
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
        return Optional.ofNullable(productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id)));
    }

    public Product save(ProductDTO productDTO) {
        var walletDb = productRepository.findByDescriptionOrBarcode(productDTO.description(), productDTO.barcode());

        if(walletDb.isPresent()) {
            throw  new ProductDataAlreadyExistsException("Description or Email already exists");
        }

        return productRepository.save(productDTO.toProduct());
    }

    public Product updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        existingProduct.setDescription(productDTO.description());
        existingProduct.setBarcode(productDTO.barcode());
        existingProduct.setUnitPrice(productDTO.unitPrice());
        existingProduct.setUnitOfMeasure(productDTO.unitOfMeasure());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com o ID: " + id));

        productRepository.delete(existingProduct);
    }
}
