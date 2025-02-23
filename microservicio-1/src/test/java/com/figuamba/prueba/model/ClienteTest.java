package com.figuamba.prueba.model;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.figuamba.prueba.model.Cliente;

class ClienteTest {

    @Test
    void crearInicializarClienteTest() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan Pérez");  
        cliente.setContrasena("1234");
        cliente.setEstado(true);

        assertNotNull(cliente);
        assertEquals("Juan Pérez", cliente.getNombre());  
        assertEquals("1234", cliente.getContrasena());
        assertTrue(cliente.getEstado());
    }

    @Test
    void actualizarEstadoClienteTest() {
        Cliente cliente = new Cliente();
        cliente.setEstado(true);        
        cliente.setEstado(false);          
        assertFalse(cliente.getEstado());  
    }
}