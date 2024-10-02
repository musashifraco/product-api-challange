package com.example.product_management_api.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductDataAlreadyExistsExceptionTest {

    @Test
    void productDataAlreadyExistsException_shouldReturnCorrectProblemDetail() {
        String detailMessage = "Description or Barcode already exists";
        ProductDataAlreadyExistsException exception = new ProductDataAlreadyExistsException(detailMessage);
        ProblemDetail problemDetail = exception.toProblemDetail();

        assertEquals("Product data already exists", problemDetail.getTitle());
        assertEquals(detailMessage, problemDetail.getDetail());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), problemDetail.getStatus());
    }
}

class ProductNotFoundExceptionTest {

    @Test
    void productNotFoundException_shouldReturnCorrectProblemDetail() {
        Long productId = 1L;
        ProductNotFoundException exception = new ProductNotFoundException(productId);
        ProblemDetail problemDetail = exception.toProblemDetail();

        assertEquals("Product not found", problemDetail.getTitle());
        assertEquals("There is no product with this id " + productId + ".", problemDetail.getDetail());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), problemDetail.getStatus());
    }
}

class ProductExceptionTest {

    @Test
    void productException_shouldReturnDefaultProblemDetail() {
        ProductException exception = new ProductException() {}; // Classe anônima para instanciar a classe abstrata
        ProblemDetail problemDetail = exception.toProblemDetail();

        assertEquals("Product internal server error", problemDetail.getTitle());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), problemDetail.getStatus());
    }
}
