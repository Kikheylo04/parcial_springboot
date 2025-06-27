package com.example.examen.service;

import com.example.examen.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Order create(Order order);
    Order update(Long id, Order order);
    void delete(Long id);
    Order getById(Long id);
    Page<Order> list(String client, String status, Pageable pageable);
}
