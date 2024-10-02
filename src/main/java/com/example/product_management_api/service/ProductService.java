package com.example.product_management_api.service;

import com.example.product_management_api.entity.Product;
import com.example.product_management_api.entity.dto.ProductDTO;
import com.example.product_management_api.exception.ProductDataAlreadyExistsException;
import com.example.product_management_api.exception.ProductNotFoundException;
import com.example.product_management_api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable("products")
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Cacheable(value = "products", key = "#id")
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id)
                .or(() -> {
                    throw new ProductNotFoundException(id);
                });
    }

    @CacheEvict(value = "products", allEntries = true)
    public Product save(ProductDTO productDTO) {
        if (productRepository.findByDescriptionOrBarcode(productDTO.description(), productDTO.barcode()).isPresent()) {
            throw new ProductDataAlreadyExistsException("Description or Barcode already exists");
        }
        return productRepository.save(productDTO.toProduct());
    }

    @CacheEvict(value = "products", allEntries = true)
    public Product updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        existingProduct.setDescription(productDTO.description());
        existingProduct.setBarcode(productDTO.barcode());
        existingProduct.setUnitPrice(productDTO.unitPrice());
        existingProduct.setUnitOfMeasure(productDTO.unitOfMeasure());

        return productRepository.save(existingProduct);
    }

    @CacheEvict(value = "products", allEntries = true)
    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productRepository.delete(existingProduct);
    }
}
