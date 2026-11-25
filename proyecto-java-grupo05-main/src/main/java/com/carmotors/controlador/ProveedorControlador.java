package com.carmotors.controlador;

import com.carmotors.modelo.Proveedor;
import com.carmotors.modelo.RepuestoTipo;
import com.carmotors.servicio.ProveedorServicio;

import java.util.List;
import java.util.Optional;

public class ProveedorControlador {

    private final ProveedorServicio proveedorServicio;

    public ProveedorControlador(ProveedorServicio proveedorServicio) {
        this.proveedorServicio = proveedorServicio;
    }

    public Proveedor registrarProveedor(String nombre, String contacto) {
        return proveedorServicio.registrarProveedor(nombre, contacto);
    }

    public List<Proveedor> obtenerTodosLosProveedores() {
        return proveedorServicio.listarProveedoresOrdenados();
    }

    public List<Proveedor> buscarProveedoresPorNombre(String termino) {
        return proveedorServicio.buscarProveedoresPorNombre(termino);
    }

    public Optional<Proveedor> buscarProveedorPorId(int id) {
        return proveedorServicio.buscarPorId(id);
    }

    public List<RepuestoTipo> obtenerTiposDeRepuesto() {
        return proveedorServicio.listarTiposRepuesto();
    }
}