package com.carmotors.vista;

import com.carmotors.controlador.ClienteControlador;
import com.carmotors.dao.impl.ClienteDaoImpl;
import com.carmotors.servicio.ClienteServicio;

import java.util.Scanner;

public class MenuPrincipal {

    private final Scanner scanner;
    private final ClienteView clienteView;

    public MenuPrincipal() {
        this.scanner = new Scanner(System.in);
        // Inicializar componentes del módulo Clientes
        ClienteDaoImpl clienteDao = new ClienteDaoImpl();
        ClienteServicio clienteServicio = new ClienteServicio(clienteDao);
        ClienteControlador clienteControlador = new ClienteControlador(clienteServicio);
        this.clienteView = new ClienteView(clienteControlador);
    }

    public void iniciar() {
        int opcion = -1;
        do {
            System.out.println("\n=========================================");
            System.out.println("   SISTEMA DE GESTIÓN TALLER CARMOTORS   ");
            System.out.println("=========================================");
            System.out.println("1. Gestión de Clientes");
            System.out.println("2. Gestión de Vehículos (Próximamente)");
            System.out.println("3. Gestión de Proveedores y Catálogos (Próximamente)");
            System.out.println("4. Gestión de Servicios y Taller (Próximamente)");
            System.out.println("5. Facturación (Próximamente)");
            System.out.println("0. Salir");
            System.out.println("=========================================");
            System.out.print("Seleccione una opción: ");

            try {
                String input = scanner.nextLine().trim();
                opcion = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[Error] Debe ingresar un número válido.");
                continue;
            }

            switch (opcion) {
                case 1:
                    clienteView.mostrarMenu();
                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                    System.out.println("[Aviso] Módulo en desarrollo.");
                    break;
                case 0:
                    System.out.println("Saliendo del sistema. ¡Hasta pronto!");
                    break;
                default:
                    System.out.println("[Error] Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }
}
