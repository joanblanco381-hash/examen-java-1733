package com.carmotors.vista;

import com.carmotors.controlador.ProveedorControlador;
import com.carmotors.modelo.Proveedor;
import com.carmotors.modelo.RepuestoTipo;

import java.util.List;
import java.util.Scanner;

public class ProveedorVista {

    private final ProveedorControlador controlador;
    private final Scanner scanner;

    public ProveedorVista(ProveedorControlador controlador) {
        this(controlador, new Scanner(System.in));
    }

    public ProveedorVista(ProveedorControlador controlador, Scanner scanner) {
        this.controlador = controlador;
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        int opcion = -1;

        do {
            System.out.println("\n=========================================");
            System.out.println("          GESTIÓN DE PROVEEDORES");
            System.out.println("=========================================");
            System.out.println("1. Registrar nuevo proveedor");
            System.out.println("2. Listar todos los proveedores");
            System.out.println("3. Buscar proveedor por nombre");
            System.out.println("4. Ver catálogo de tipos de repuestos");
            System.out.println("0. Volver al menú principal");
            System.out.println("=========================================");
            System.out.print("Seleccione una opción: ");

            String entrada = scanner.nextLine().trim();
            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("[Error] Por favor, ingrese un número válido.");
                continue;
            }

            switch (opcion) {
                case 1 -> registrarProveedor();
                case 2 -> listarProveedores();
                case 3 -> buscarProveedorPorNombre();
                case 4 -> verCatalogoTiposRepuesto();
                case 0 -> System.out.println("[Info] Volviendo al menú principal...");
                default -> System.out.println("[Error] Opción no válida. Intente nuevamente.");
            }
        } while (opcion != 0);
    }

    private void registrarProveedor() {
        System.out.println("\n--- REGISTRAR NUEVO PROVEEDOR ---");
        System.out.print("Ingrese el nombre o razón social del proveedor: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese los datos de contacto (Teléfono / Email / Asesor): ");
        String contacto = scanner.nextLine();

        try {
            Proveedor registrado = controlador.registrarProveedor(nombre, contacto);
            System.out.println("[Éxito] Proveedor registrado con ID: " + registrado.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("[Validación] " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("[Error] No se pudo registrar el proveedor: " + e.getMessage());
        }
    }

    private void listarProveedores() {
        System.out.println("\n--- LISTADO DE PROVEEDORES (ORDEN ALFABÉTICO) ---");
        try {
            List<Proveedor> proveedores = controlador.obtenerTodosLosProveedores();
            if (proveedores.isEmpty()) {
                System.out.println("[Info] No hay proveedores registrados actualmente.");
                return;
            }

            System.out.printf("%-5s | %-30s | %-35s%n", "ID", "NOMBRE", "CONTACTO");
            System.out.println("---------------------------------------------------------------------------");
            for (Proveedor p : proveedores) {
                System.out.printf("%-5d | %-30s | %-35s%n",
                        p.getId(),
                        p.getNombre(),
                        p.getContacto() != null ? p.getContacto() : "Sin contacto");
            }
        } catch (RuntimeException e) {
            System.out.println("[Error] Error al consultar proveedores: " + e.getMessage());
        }
    }

    private void buscarProveedorPorNombre() {
        System.out.println("\n--- BUSCAR PROVEEDOR POR NOMBRE ---");
        System.out.print("Ingrese el nombre o palabra clave a buscar: ");
        String termino = scanner.nextLine();

        try {
            List<Proveedor> resultados = controlador.buscarProveedoresPorNombre(termino);
            if (resultados.isEmpty()) {
                System.out.println("[Info] No se encontraron proveedores con ese criterio.");
                return;
            }

            System.out.printf("%-5s | %-30s | %-35s%n", "ID", "NOMBRE", "CONTACTO");
            System.out.println("---------------------------------------------------------------------------");
            for (Proveedor p : resultados) {
                System.out.printf("%-5d | %-30s | %-35s%n",
                        p.getId(),
                        p.getNombre(),
                        p.getContacto() != null ? p.getContacto() : "Sin contacto");
            }
        } catch (RuntimeException e) {
            System.out.println("[Error] Error al buscar proveedores: " + e.getMessage());
        }
    }

    private void verCatalogoTiposRepuesto() {
        System.out.println("\n--- CATÁLOGO DE TIPOS DE REPUESTOS ---");
        try {
            List<RepuestoTipo> tipos = controlador.obtenerTiposDeRepuesto();
            if (tipos.isEmpty()) {
                System.out.println("[Info] No hay tipos de repuestos registrados en el catálogo.");
                return;
            }

            System.out.printf("%-5s | %-25s%n", "ID", "TIPO DE REPUESTO");
            System.out.println("----------------------------------");
            for (RepuestoTipo tipo : tipos) {
                System.out.printf("%-5d | %-25s%n", tipo.getId(), tipo.getNombre());
            }
        } catch (RuntimeException e) {
            System.out.println("[Error] Error al consultar el catálogo: " + e.getMessage());
        }
    }
}