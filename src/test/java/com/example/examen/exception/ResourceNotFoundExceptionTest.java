package com.example.examen.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceNotFoundExceptionTest {

    @Test
    void testMessage() {
        String mensaje = "Recurso no encontrado";
        ResourceNotFoundException ex = new ResourceNotFoundException(mensaje);
        assertEquals(mensaje, ex.getMessage());
    }
}
