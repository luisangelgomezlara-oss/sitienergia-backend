package com.nexuscode.registro.repositories;

import com.nexuscode.registro.entities.MantenimientoProgramado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MantenimientoProgramadoRepository extends JpaRepository<MantenimientoProgramado, Long> {
    // Método para traer solo los mantenimientos activos o futuros
    List<MantenimientoProgramado> findByEstadoInOrderByFechaInicioAsc(List<String> estados);
}