package com.carmotors.servicio;

import com.carmotors.DAO.ProveedorDao;
import com.carmotors.DAO.RepuestoTipoDao;
import com.carmotors.modelo.Proveedor;
import com.carmotors.modelo.RepuestoTipo;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProveedorServicio {

    private final ProveedorDao proveedorDao;
    private final RepuestoTipoDao repuestoTipoDao;

    // Inyección de dependencias por constructor (Principio de Inversión de Dependencias - SOLID)
    public ProveedorServicio(ProveedorDao proveedorDao, RepuestoTipoDao repuestoTipoDao) {
        this.proveedorDao = proveedorDao;
        this.repuestoTipoDao = repuestoTipoDao;
    }

    public Proveedor registrarProveedor(String nombre, String contacto) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del proveedor no puede estar vacío.");
        }
        if (contacto == null || contacto.isBlank()) {
            throw new IllegalArgumentException("La información de contacto del proveedor es obligatoria.");
        }

        Proveedor nuevoProveedor = new Proveedor(nombre.trim(), contacto.trim());
        proveedorDao.guardar(nuevoProveedor);
        return nuevoProveedor;
    }

    public List<Proveedor> listarProveedoresOrdenados() {
        return proveedorDao.listarTodos().stream()
                .sorted(Comparator.comparing(Proveedor::getNombre, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public List<Proveedor> buscarProveedoresPorNombre(String termino) {
        if (termino == null || termino.isBlank()) {
            return listarProveedoresOrdenados();
        }
        String criterio = termino.trim().toLowerCase();

        return proveedorDao.listarTodos().stream()
                .filter(p -> p.getNombre() != null && p.getNombre().toLowerCase().contains(criterio))
                .sorted(Comparator.comparing(Proveedor::getNombre, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public Optional<Proveedor> buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del proveedor debe ser mayor a cero.");
        }
        return proveedorDao.buscarPorId(id);
    }

    public List<RepuestoTipo> listarTiposRepuesto() {
        return repuestoTipoDao.listarTodos().stream()
                .sorted(Comparator.comparing(RepuestoTipo::getId))
                .collect(Collectors.toList());
    }
}