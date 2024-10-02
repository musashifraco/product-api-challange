package com.example.product_management_api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component("apiKeyFilter") // Especifica o nome do bean
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${api.key}")
    private String apiKey;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String apiKeyHeader = request.getHeader("API-Key");
        String requestURI = request.getRequestURI();


        if (requestURI.startsWith("/swagger-ui") || requestURI.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (apiKeyHeader == null || !apiKeyHeader.equals(apiKey)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "API Key inválida");
            return;
        }

        filterChain.doFilter(request, response);
    }
}