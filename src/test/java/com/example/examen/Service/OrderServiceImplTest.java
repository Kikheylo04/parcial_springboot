package com.example.examen.service;

import com.example.examen.exception.ProductNotFoundException;
import com.example.examen.model.Order;
import com.example.examen.model.OrderItem;
import com.example.examen.model.Product;
import com.example.examen.repository.OrderRepository;
import com.example.examen.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder_Success() {
        Product product = new Product();
        product.setId(1L);
        product.setPrice(BigDecimal.TEN);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(2);

        Order order = new Order();
        order.setItems(List.of(item));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.create(order);

        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testUpdateOrder_Success() {
        Long id = 1L;

        Product product = new Product();
        product.setId(1L);
        product.setPrice(BigDecimal.TEN);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(3);

        Order updatedOrder = new Order();
        updatedOrder.setItems(List.of(item));

        Order existingOrder = new Order();
        existingOrder.setId(id);
        existingOrder.setItems(new ArrayList<>());

        when(orderRepository.findById(id)).thenReturn(Optional.of(existingOrder));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(existingOrder);

        Order result = orderService.update(id, updatedOrder);

        assertNotNull(result);
        verify(orderRepository).save(existingOrder);
    }

    @Test
    void testGetById_ReturnsOrder() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testDelete_Success() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.delete(1L);

        verify(orderRepository).delete(order);
    }

    @Test
    void testCreateOrder_ProductNotFound_ThrowsException() {
        Product product = new Product();
        product.setId(999L);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(1);

        Order order = new Order();
        order.setItems(List.of(item));

        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> orderService.create(order));
    }

    @Test
    void testCreateOrder_NullProduct_ThrowsInvalidParameterException() {
        OrderItem item = new OrderItem();
        item.setProduct(null);

        Order order = new Order();
        order.setItems(List.of(item));

        assertThrows(InvalidParameterException.class, () -> orderService.create(order));
    }

    @Test
    void testCreateOrder_NullProductId_ThrowsInvalidParameterException() {
        Product product = new Product();
        OrderItem item = new OrderItem();
        item.setProduct(product);

        Order order = new Order();
        order.setItems(List.of(item));

        assertThrows(InvalidParameterException.class, () -> orderService.create(order));
    }

    @Test
    void testUpdateOrder_NullProduct_ThrowsInvalidParameterException() {
        Long id = 1L;

        OrderItem item = new OrderItem();
        item.setProduct(null);

        Order updatedOrder = new Order();
        updatedOrder.setItems(List.of(item));

        Order existingOrder = new Order();
        existingOrder.setId(id);
        existingOrder.setItems(new ArrayList<>());

        when(orderRepository.findById(id)).thenReturn(Optional.of(existingOrder));

        assertThrows(InvalidParameterException.class, () -> orderService.update(id, updatedOrder));
    }

    @Test
    void testUpdateOrder_NullProductId_ThrowsInvalidParameterException() {
        Long id = 1L;

        Product product = new Product();
        OrderItem item = new OrderItem();
        item.setProduct(product);

        Order updatedOrder = new Order();
        updatedOrder.setItems(List.of(item));

        Order existingOrder = new Order();
        existingOrder.setId(id);
        existingOrder.setItems(new ArrayList<>());

        when(orderRepository.findById(id)).thenReturn(Optional.of(existingOrder));

        assertThrows(InvalidParameterException.class, () -> orderService.update(id, updatedOrder));
    }

    @Test
    void testUpdateOrder_ProductNotFound_ThrowsException() {
        Long id = 1L;

        Product product = new Product();
        product.setId(999L);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(1);

        Order updatedOrder = new Order();
        updatedOrder.setItems(List.of(item));

        Order existingOrder = new Order();
        existingOrder.setId(id);
        existingOrder.setItems(new ArrayList<>());

        when(orderRepository.findById(id)).thenReturn(Optional.of(existingOrder));
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> orderService.update(id, updatedOrder));
    }

    @Test
    void testListOrders_ReturnsPage() {
        Pageable pageable = mock(Pageable.class);
        Page<Order> orderPage = mock(Page.class);

        when(orderRepository.findAll(pageable)).thenReturn(orderPage);

        Page<Order> result = orderService.list("cliente", "activo", pageable);

        assertNotNull(result);
        verify(orderRepository).findAll(pageable);
    }

}
