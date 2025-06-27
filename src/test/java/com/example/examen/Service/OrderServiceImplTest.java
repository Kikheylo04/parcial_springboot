package com.example.examen.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.*;

import com.example.examen.exception.ProductNotFoundException;
import com.example.examen.model.*;
import com.example.examen.repository.OrderRepository;
import com.example.examen.repository.ProductRepository;
import com.example.examen.service.OrderServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetById_ReturnsOrder() {
        Order mockOrder = new Order();
        mockOrder.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));

        Order result = orderService.getById(1L);
        assertEquals(1L, result.getId());
        verify(orderRepository).findById(1L);
    }

    @Test
    void testDelete_RemovesOrder() {
        Order mockOrder = new Order();
        mockOrder.setId(2L);
        when(orderRepository.findById(2L)).thenReturn(Optional.of(mockOrder));

        orderService.delete(2L);
        verify(orderRepository).delete(mockOrder);
    }

    @Test
    void testCreate_CalculatesTotalCorrectly() {
        Product product = new Product();
        product.setId(1L);
        product.setPrice(new BigDecimal("10"));

        OrderItem item = new OrderItem();
        item.setQuantity(2);
        item.setProduct(product);

        Order order = new Order();
        order.setItems(List.of(item));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Order createdOrder = orderService.create(order);

        assertEquals(new BigDecimal("20"), createdOrder.getTotal());
        verify(orderRepository).save(order);
    }

    @Test
    void testCreate_ThrowsProductNotFoundException() {
        Product product = new Product();
        product.setId(99L);

        OrderItem item = new OrderItem();
        item.setQuantity(1);
        item.setProduct(product);

        Order order = new Order();
        order.setItems(List.of(item));

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> orderService.create(order));
    }
}
