package com.example.product_management_api.controller;

import com.example.product_management_api.entity.Product;
import com.example.product_management_api.entity.dto.ProductDTO;
import com.example.product_management_api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final RedisTemplate<String, List<Product>> redisTemplate;

    @Autowired
    public ProductController(ProductService productService, RedisTemplate<String, List<Product>> redisTemplate) {
        this.productService = productService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        List<Product> products = productService.findAll();
        // Armazena todos os produtos em cache
        redisTemplate.opsForValue().set("products", products);
        return products;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        // Tenta obter o produto do cache
        List<Product> cachedProducts = redisTemplate.opsForValue().get("products");
        if (cachedProducts != null) {
            return cachedProducts.stream()
                    .filter(product -> product.getId().equals(id))
                    .findFirst()
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        // Se não estiver no cache, busca no serviço
        return productService.findById(id)
                .map(product -> {
                    // Armazena o produto no cache
                    cacheProduct(product);
                    return ResponseEntity.ok().body(product);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        var savedProduct = productService.save(productDTO);
        // Invalida o cache após criar um novo produto
        redisTemplate.delete("products");
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        var updatedProduct = productService.updateProduct(id, productDTO);
        // Invalida o cache após atualizar o produto
        redisTemplate.delete("products");
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        // Invalida o cache após deletar um produto
        redisTemplate.delete("products");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cache-status")
    public ResponseEntity<String> checkCache() {
        // Verifica se a chave 'products' está presente no Redis
        if (redisTemplate.hasKey("products")) {
            return ResponseEntity.ok("Cache está ativo e contém produtos.");
        } else {
            return ResponseEntity.ok("Cache não contém produtos.");
        }
    }

    private void cacheProduct(Product product) {
        // Recupera a lista de produtos do cache ou cria uma nova lista
        List<Product> cachedProducts = redisTemplate.opsForValue().get("products");
        if (cachedProducts != null) {
            cachedProducts.add(product);
        } else {
            cachedProducts = List.of(product); // Cria uma nova lista se o cache estiver vazio
        }
        // Atualiza o cache com a nova lista de produtos
        redisTemplate.opsForValue().set("products", cachedProducts);
    }
}
