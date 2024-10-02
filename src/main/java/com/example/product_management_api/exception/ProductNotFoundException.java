package com.example.product_management_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ProductNotFoundException extends ProductException {
    private final Long productId;

    public ProductNotFoundException(Long walletId) {
        this.productId = walletId;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        pb.setTitle("Product not found");
        pb.setDetail("There is no product with this id " + productId + ".");

        return pb;
    }
}