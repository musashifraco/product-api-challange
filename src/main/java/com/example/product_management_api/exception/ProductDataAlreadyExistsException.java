package com.example.product_management_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ProductDataAlreadyExistsException extends ProductException{

    private final String detail;

    public ProductDataAlreadyExistsException(String detail) {
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        pb.setTitle("Product data already exists");
        pb.setDetail(detail);

        return pb;
    }
}