package com.example.product_management_api.controller;

import com.example.product_management_api.entity.Product;
import com.example.product_management_api.entity.dto.ProductDTO;
import com.example.product_management_api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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


    @Operation(summary = "Finds all product",
            description = "Finds all product",
            tags = {"Product"},
            responses = {@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
                    @ApiResponse(description = "Success",
                            responseCode = "200",
                            content = {@Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Product.class)))})})
    @GetMapping
    public List<Product> getAllProducts() {
        List<Product> products = productService.findAll();
        redisTemplate.opsForValue().set("products", products);
        return products;
    }


    @Operation(summary = "Finds a product",
            description = "Finds a product",
            tags = {"Product"},
            responses = {@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
                    @ApiResponse(description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Product.class))
                    ),
                    @ApiResponse(description = "No Content",
                            responseCode = "204",
                            content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        List<Product> cachedProducts = redisTemplate.opsForValue().get("products");
        if (cachedProducts != null) {
            return cachedProducts.stream()
                    .filter(product -> product.getId().equals(id))
                    .findFirst()
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        return productService.findById(id)
                .map(product -> {
                    cacheProduct(product);
                    return ResponseEntity.ok().body(product);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Adds a product",
            description = "Adds a new product",
            tags = {"Product"},
            responses = {@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
                    @ApiResponse(description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Product.class)))
            })
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        var savedProduct = productService.save(productDTO);
        redisTemplate.delete("products");
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @Operation(summary = "Updates a product",
            description = "Updates a new product",
            tags = {"Product"},
            responses = {@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Product.class)))
            })
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        var updatedProduct = productService.updateProduct(id, productDTO);
        redisTemplate.delete("products");
        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(summary = "Deletes a product",
            description = "Deletes a new product",
            tags = {"Product"},
            responses = {@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content)
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        redisTemplate.delete("products");
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Finds cache",
            description = "Finds cache",
            tags = {"Cache"},
            responses = {@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
                    @ApiResponse(description = "Success",
                            responseCode = "200",
                            content = @Content
                    ),
                    @ApiResponse(description = "No Content",
                            responseCode = "204",
                            content = @Content)})
    @GetMapping("/cache-status")
    public ResponseEntity<String> checkCache() {
        if (redisTemplate.hasKey("products")) {
            return ResponseEntity.ok("Cache está ativo e contém produtos.");
        } else {
            return ResponseEntity.ok("Cache não contém produtos.");
        }
    }

    private void cacheProduct(Product product) {
        List<Product> cachedProducts = redisTemplate.opsForValue().get("products");
        if (cachedProducts != null) {
            cachedProducts.add(product);
        } else {
            cachedProducts = List.of(product);
        }
        redisTemplate.opsForValue().set("products", cachedProducts);
    }
}
