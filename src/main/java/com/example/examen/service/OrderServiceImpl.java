package com.example.examen.service;

import java.math.BigDecimal;
import java.security.InvalidParameterException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.examen.exception.OrderNotFoundException;
import com.example.examen.exception.ProductNotFoundException;
import com.example.examen.model.Order;
import com.example.examen.model.OrderItem;
import com.example.examen.model.Product;
import com.example.examen.repository.OrderRepository;
import com.example.examen.repository.ProductRepository;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Order create(Order order) {
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : order.getItems()) {
            if (item.getProduct() == null || item.getProduct().getId() == null) {
                throw new InvalidParameterException();
            }

            Product realProduct = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(
                            () -> new ProductNotFoundException(item.getProduct().getId()));
                    

            item.setProduct(realProduct);
            item.setOrder(order);

            BigDecimal subtotal = realProduct.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(subtotal);
        }

        order.setTotal(total);
        return orderRepository.save(order);
    }

    @Override
    public Order update(Long id, Order updatedOrder) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        existingOrder.setClient(updatedOrder.getClient());
        existingOrder.setDate(updatedOrder.getDate());
        existingOrder.setStatus(updatedOrder.getStatus());

        existingOrder.getItems().clear();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : updatedOrder.getItems()) {
            if (item.getProduct() == null || item.getProduct().getId() == null) {
                throw new InvalidParameterException();
            }

            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(
                            () -> new ProductNotFoundException(item.getProduct().getId())
                    );

            item.setProduct(product);
            item.setOrder(existingOrder);

            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(subtotal);

            existingOrder.getItems().add(item);
        }

        existingOrder.setTotal(total);
        return orderRepository.save(existingOrder);
    }

    @Override
    public void delete(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));
        orderRepository.delete(order);
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));
    }

    @Override
    public Page<Order> list(String client, String status, Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
}
