package com.example.examen.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.examen.model.Product;
import com.example.examen.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        product.setId(id);
        return productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Product> list(String name, Pageable pageable) {
        if (name != null && !name.isEmpty()) {
            // Aquí puedes implementar búsqueda por nombre si tienes un método custom
            return productRepository.findAll(pageable); // simple por ahora
        }
        return productRepository.findAll(pageable);
    }

    @Override
    public Integer getStockFromExternalService(Long productId) {
        // Simulación
        return 0;
    }
}
