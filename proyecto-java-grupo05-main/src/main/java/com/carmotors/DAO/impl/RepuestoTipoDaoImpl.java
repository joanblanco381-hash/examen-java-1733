package com.carmotors.DAO.impl;

import com.carmotors.DAO.RepuestoTipoDao;
import com.carmotors.modelo.RepuestoTipo;
import com.carmotors.config.ConexionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepuestoTipoDaoImpl implements RepuestoTipoDao {

    @Override
    public void guardar(RepuestoTipo tipo) {
        String sql = "INSERT INTO repuesto_tipos (nombre) VALUES (?)";

        try (Connection conn = ConexionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, tipo.getNombre());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        tipo.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el tipo de repuesto: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<RepuestoTipo> buscarPorId(int id) {
        String sql = "SELECT id, nombre FROM repuesto_tipos WHERE id = ?";

        try (Connection conn = ConexionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearRepuestoTipo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar tipo de repuesto por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<RepuestoTipo> listarTodos() {
        String sql = "SELECT id, nombre FROM repuesto_tipos";
        List<RepuestoTipo> tipos = new ArrayList<>();



        try (Connection conn = ConexionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tipos.add(mapearRepuestoTipo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los tipos de repuesto: " + e.getMessage(), e);
        }
        return tipos;
    }

    private RepuestoTipo mapearRepuestoTipo(ResultSet rs) throws SQLException {
        return new RepuestoTipo(
                rs.getInt("id"),
                rs.getString("nombre")
        );
    }
}