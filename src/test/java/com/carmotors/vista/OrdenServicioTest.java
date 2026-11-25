package com.carmotors.vista;

import com.carmotors.controlador.ServicioControlador;
import com.carmotors.dao.ServicioDao;
import com.carmotors.modelo.Cliente;
import com.carmotors.modelo.Servicio;
import com.carmotors.modelo.ServicioRepuesto;
import com.carmotors.modelo.Vehiculo;
import com.carmotors.servicio.InventarioServiceMock;
import com.carmotors.servicio.TallerServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrdenServicioTest {

    @Mock
    private ServicioDao servicioDao;

    private ServicioControlador servicioControlador;

    @BeforeEach
    void setUp() {
        TallerServicio tallerServicio = new TallerServicio(servicioDao, new InventarioServiceMock());
        servicioControlador = new ServicioControlador(tallerServicio);
    }

    @Test
    @DisplayName("Muestra el nombre del archivo generado cuando la orden existe")
    void testExportarOrdenExitosa() throws Exception {
        int servicioId = 15;
        Servicio orden = new Servicio(
                servicioId, 10, 1, LocalDateTime.of(2025, 8, 29, 19, 55),
                null, "Cambio de frenos delanteros", 150000.0, Servicio.ESTADO_EN_PROCESO
        );
        orden.setCliente(new Cliente(1, "Carlos Perez", "123456789", "3001112233", "carlos@mail.com"));
        orden.setVehiculo(new Vehiculo(10, 1, "ABC123", "Toyota", "Corolla", 2020, 45000, true));
        ServicioRepuesto r = new ServicioRepuesto(servicioId, 1, 2, 80000.0);
        r.setNombreRepuesto("Pastillas de Freno Delanteras");
        orden.setRepuestos(List.of(r));

        when(servicioDao.buscarOrdenDetalladaPorId(servicioId)).thenReturn(orden);

        Path path = Path.of("orden_servicio_15.txt");
        try {
            String salida = ejecutarConEntrada("15\n");
            assertTrue(salida.contains("orden_servicio_15.txt"));
            assertTrue(salida.contains("[Éxito]"));
            assertTrue(Files.exists(path));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    @DisplayName("Muestra un aviso amigable cuando el ID de la orden no existe")
    void testExportarOrdenNoExiste() {
        when(servicioDao.buscarOrdenDetalladaPorId(999)).thenReturn(null);

        String salida = ejecutarConEntrada("999\n");

        assertTrue(salida.contains("[Aviso]"));
        assertTrue(salida.contains("999"));
    }

    @Test
    @DisplayName("Muestra error cuando el ID ingresado no es numérico")
    void testExportarOrdenIdInvalido() {
        String salida = ejecutarConEntrada("abc\n");

        assertTrue(salida.contains("[Error]"));
    }

    private String ejecutarConEntrada(String entrada) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(entrada.getBytes(StandardCharsets.UTF_8)));
        PrintStream salidaOriginal = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            OrdenServicio vista = new OrdenServicio(servicioControlador, scanner);
            vista.exportarOrden();
        } finally {
            System.setOut(salidaOriginal);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }
}
