package com.carmotors.DAO.impl;

import com.carmotors.DAO.ProveedorDao;
import com.carmotors.modelo.Proveedor;
import com.carmotors.config.ConexionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProveedorDaoImpl implements ProveedorDao {

    @Override
    public void guardar(Proveedor proveedor) {
        String sql = "INSERT INTO proveedores (nombre, contacto) VALUES (?, ?)";

        try (Connection conn = ConexionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getContacto());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        proveedor.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el proveedor: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Proveedor> buscarPorId(int id) {
        String sql = "SELECT id, nombre, contacto FROM proveedores WHERE id = ?";

        try (Connection conn = ConexionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearProveedor(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar proveedor por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Proveedor> listarTodos() {
        String sql = "SELECT id, nombre, contacto FROM proveedores";
        List<Proveedor> proveedores = new ArrayList<>();

        try (Connection conn = ConexionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                proveedores.add(mapearProveedor(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los proveedores: " + e.getMessage(), e);
        }
        return proveedores;
    }

    @Override
    public void actualizar(Proveedor proveedor) {
        String sql = "UPDATE proveedores SET nombre = ?, contacto = ? WHERE id = ?";

        try (Connection conn = ConexionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getContacto());
            stmt.setInt(3, proveedor.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el proveedor: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM proveedores WHERE id = ?";

        try (Connection conn = ConexionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el proveedor: " + e.getMessage(), e);
        }
    }

    private Proveedor mapearProveedor(ResultSet rs) throws SQLException {
        return new Proveedor(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("contacto")
        );
    }
}