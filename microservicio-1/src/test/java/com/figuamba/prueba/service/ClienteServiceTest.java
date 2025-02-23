package com.figuamba.prueba.service;

import static org.assertj.core.api.Assertions.*;

import com.figuamba.prueba.exception.EntityNotFoundException;
import com.figuamba.prueba.model.Cliente;
import com.figuamba.prueba.repository.ClienteRepository;
import com.figuamba.prueba.service.ClienteService;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Transactional 
public class ClienteServiceTest {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente clienteTest;

    @BeforeEach
    void setUp() {
        clienteTest = new Cliente();
        clienteTest.setContrasena("1234");
        clienteTest.setNombre("Jose Paez");
        clienteTest.setEstado(true);
        clienteTest.setEdad(50);
        clienteTest.setGenero("Masculino");
        clienteTest.setIdentificacion("123456789");
        clienteTest.setDireccion("Guayaquil");
        clienteTest.setTelefono("99999999");
        clienteRepository.save(clienteTest);
    }

    @AfterEach
    void tearDown() {
        clienteRepository.deleteAll();
    }

    @Test
    void listarClientesTest() {
        List<Cliente> clientes = clienteService.listarClientes();
        assertThat(clientes).isNotEmpty();
    }

    @Test
    void obtenerClienteTest() {
        Cliente clienteGuardado = clienteRepository.save(clienteTest);
        Cliente clienteObtenido = clienteService.obtenerCliente(clienteGuardado.getId());

        assertThat(clienteObtenido).isNotNull();
        assertThat(clienteObtenido.getId()).isEqualTo(clienteGuardado.getId());
    }

    @Test
    void obtenerClienteNoExisteTest() {
        assertThatThrownBy(() -> clienteService.obtenerCliente(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Registro(s) no encontrado(s). ");
    }

    @Test
    void guardarClienteTest() {
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setContrasena("abcd");
        nuevoCliente.setEstado(true);
        Cliente clienteGuardado = clienteService.guardarCliente(clienteTest);
        assertThat(clienteGuardado).isNotNull();
        assertThat(clienteGuardado.getId()).isNotNull();
    }

    @Test
    void actualizarClienteTest() {
        Cliente clienteGuardado = clienteRepository.save(clienteTest);
        clienteGuardado.setContrasena("147258");
        Cliente clienteActualizado = clienteService.actualizarCliente(clienteGuardado);
        assertThat(clienteActualizado.getContrasena()).isEqualTo("147258");
    }

    @Test
    void actualizarClienteNoExisteTest() {
        Cliente clienteNoExistente = new Cliente();
        clienteNoExistente.setId(999L);
        clienteNoExistente.setContrasena("147258");

        assertThatThrownBy(() -> clienteService.actualizarCliente(clienteNoExistente))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Registro(s) no encontrado(s). ");
    }

    @Test
    void eliminarClienteTest() {
        Cliente cliente = clienteRepository.save(clienteTest);
        clienteService.eliminarCliente(cliente.getId());
        assertThat(clienteRepository.findById(cliente.getId())).isEmpty();
    }
}
