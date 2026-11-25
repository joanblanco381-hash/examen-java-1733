package com.carmotors.DAO;

import com.carmotors.modelo.RepuestoTipo;
import java.util.List;
import java.util.Optional;

public interface RepuestoTipoDao {
    void guardar(RepuestoTipo tipo);
    Optional<RepuestoTipo> buscarPorId(int id);
    List<RepuestoTipo> listarTodos();
}