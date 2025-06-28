package com.example.examen.service;

import com.example.examen.model.Product;
import com.example.examen.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDelete() {
        Long productId = 1L;
        doNothing().when(productRepository).deleteById(productId);

        productService.delete(productId);

        verify(productRepository).deleteById(productId);
    }

    @Test
    void testGetById_Found() {
        Product product = new Product();
        product.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testList_WithName() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of());
        when(productRepository.findAll(pageable)).thenReturn(page);

        Page<Product> result = productService.list("some-name", pageable);

        assertNotNull(result);
        verify(productRepository).findAll(pageable);
    }

    @Test
    void testList_WithoutName() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of());
        when(productRepository.findAll(pageable)).thenReturn(page);

        Page<Product> result = productService.list(null, pageable);

        assertNotNull(result);
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetStockFromExternalService() {
        Integer stock = productService.getStockFromExternalService(1L);
        assertEquals(0, stock);
    }
}
