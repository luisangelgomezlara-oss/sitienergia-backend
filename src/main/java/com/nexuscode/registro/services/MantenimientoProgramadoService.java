package com.nexuscode.registro.services;

import com.nexuscode.registro.entities.MantenimientoProgramado;
import java.util.List;
import java.util.Optional;

public interface MantenimientoProgramadoService {
    List<MantenimientoProgramado> obtenerMantenimientosActivos();
    List<MantenimientoProgramado> obtenerTodos();
    MantenimientoProgramado crearMantenimiento(MantenimientoProgramado mantenimiento);
    Optional<MantenimientoProgramado> actualizarEstado(Long id, String nuevoEstado);
}