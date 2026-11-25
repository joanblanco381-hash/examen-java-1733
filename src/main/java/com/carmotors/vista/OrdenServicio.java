package com.carmotors.vista;

import com.carmotors.controlador.ServicioControlador;

import java.util.Scanner;

public class OrdenServicio {

    private final ServicioControlador servicioControlador;
    private final Scanner scanner;

    public OrdenServicio(ServicioControlador servicioControlador, Scanner scanner) {
        this.servicioControlador = servicioControlador;
        this.scanner = scanner;
    }

    public void exportarOrden() {
        System.out.println("\n--- EXPORTAR ORDEN DE SERVICIO ---");
        System.out.print("Ingrese el ID del servicio que desea exportar: ");
        String entrada = scanner.nextLine().trim();

        int servicioId;
        try {
            servicioId = Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            System.out.println("[Error] El ID del servicio debe ser un número entero válido.");
            return;
        }

        try {
            String nombreArchivo = servicioControlador.exportarOrdenServicio(servicioId);
            System.out.println("[Éxito] Orden de servicio exportada correctamente.");
            System.out.println("Archivo generado: " + nombreArchivo);
        } catch (IllegalArgumentException e) {
            System.out.println("[Aviso] " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("[Error] " + e.getMessage());
        }
    }
}
