package com.example.examen.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.examen.model.Product;

@Service
public interface ProductService {
    Product create(Product product);
    Product update(Long id, Product product);
    void delete(Long id);
    Product getById(Long id);
    Page<Product> list(String name, Pageable pageable);
    
    Integer getStockFromExternalService(Long productId);
}
