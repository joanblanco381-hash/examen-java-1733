package com.carmotors.DAO.impl;

import com.carmotors.config.ConexionFactory;
import com.carmotors.DAO.VehiculoDao;
import com.carmotors.modelo.Vehiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehiculoDaoImpl implements VehiculoDao {

    @Override
    public void guardar(Vehiculo vehiculo) {
        String sql = "INSERT INTO vehiculos (cliente_id, placa, marca, modelo, anio, kilometraje, activo) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, vehiculo.getClienteId());
            stmt.setString(2, vehiculo.getPlaca());
            stmt.setString(3, vehiculo.getMarca());
            stmt.setString(4, vehiculo.getModelo());
            stmt.setInt(5, vehiculo.getAnio());
            stmt.setInt(6, vehiculo.getKilometraje());
            stmt.setBoolean(7, vehiculo.isActivo());

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        vehiculo.setId(keys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el vehículo: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Vehiculo> buscarPorId(int id) {
        String sql = "SELECT id, cliente_id, placa, marca, modelo, anio, kilometraje, activo FROM vehiculos WHERE id = ?";

        try (Connection conn = ConexionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearVehiculo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar vehículo por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Vehiculo> buscarPorPlaca(String placa) {
        String sql = "SELECT id, cliente_id, placa, marca, modelo, anio, kilometraje, activo FROM vehiculos WHERE placa = ?";

        try (Connection conn = ConexionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, placa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearVehiculo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar vehículo por placa: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Vehiculo> listarPorClienteId(int clienteId) {
        String sql = "SELECT id, cliente_id, placa, marca, modelo, anio, kilometraje, activo FROM vehiculos WHERE cliente_id = ?";
        List<Vehiculo> lista = new ArrayList<>();

        try (Connection conn = ConexionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearVehiculo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar vehículos por cliente ID: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Vehiculo> listarTodos() {
        String sql = "SELECT id, cliente_id, placa, marca, modelo, anio, kilometraje, activo FROM vehiculos";
        List<Vehiculo> lista = new ArrayList<>();

        try (Connection conn = ConexionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearVehiculo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todos los vehículos: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public void actualizarKilometraje(int vehiculoId, int nuevoKilometraje) {
        String sql = "UPDATE vehiculos SET kilometraje = ? WHERE id = ?";

        try (Connection conn = ConexionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nuevoKilometraje);
            stmt.setInt(2, vehiculoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar kilometraje del vehículo: " + e.getMessage(), e);
        }
    }

    private Vehiculo mapearVehiculo(ResultSet rs) throws SQLException {
        return new Vehiculo(
                rs.getInt("id"),
                rs.getInt("cliente_id"),
                rs.getString("placa"),
                rs.getString("marca"),
                rs.getString("modelo"),
                rs.getInt("anio"),
                rs.getInt("kilometraje"),
                rs.getBoolean("activo")
        );
    }
}