package com.example.examen.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductNotFoundExceptionTest {

    @Test
    void shouldContainCorrectMessage() {
        Long id = 10L;
        ProductNotFoundException exception = new ProductNotFoundException(id);
        assertEquals("Producto no encontrado con ID: " + id, exception.getMessage());
    }
}
